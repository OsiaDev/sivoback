package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import com.coljuegos.sivo.service.reporte.ActaReporteService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActaNotificacionServiceImpl implements ActaNotificacionService {

    private final ActaReporteService actaReporteService;
    private final ActaReporteContextMapper actaReporteContextMapper;
    private final JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${acta.notificacion.remitente:no-reply@coljuegos.gov.co}")
    private String remitente;

    private static final String EMAIL_PATTERN = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]";

    @Override
    @Async("actaNotificacionExecutor")
    public void notificarActaAsync(SiiAutoComisorioEntity autoComisorio,
            SiiActaVisitaEntity actaVisita,
            SiiVerificacionContractualEntity contractual,
            SiiVerificacionSiplaftEntity siplaft,
            SiiVerificacionJuegoResponsableEntity juegoResponsableEntity,
            SiiFirmaActaEntity firma,
            SiiResumenInventarioEntity resumen,
            List<SiiInventarioRegistradoEntity> inventarios,
            List<SiiNovedadRegistradaEntity> novedades) {
        Integer numActa = autoComisorio.getAucNumero();
        log.info("[NOTIF] Iniciando notificación asíncrona para acta {}", numActa);

        try {
            // 1. Construir contexto y generar PDF en memoria
            ActaReporteContextDTO context = actaReporteContextMapper.mapear(
                    autoComisorio, actaVisita, contractual, siplaft, juegoResponsableEntity, firma, resumen,
                    inventarios, novedades);

            byte[] pdf = actaReporteService.generarReporteActa(context);

            if (pdf == null || pdf.length == 0) {
                log.error("[NOTIF] No se pudo generar el PDF para acta {}. Se cancela el envío.", numActa);
                return;
            }

            // 2. Resolver destinatarios
            List<String> destinatarios = resolverDestinatarios(actaVisita, autoComisorio);

            if (destinatarios.isEmpty()) {
                log.warn("[NOTIF] No hay destinatarios válidos para acta {}. Se omite el envío.", numActa);
                return;
            }

            log.info("[NOTIF] Enviando acta {} a {} destinatario(s): {}",
                    numActa, destinatarios.size(), destinatarios);

            // 3. Enviar correo con el adjunto
            enviarCorreo(destinatarios, pdf, numActa);

            log.info("[NOTIF] Notificación completada satisfactoriamente para acta {}", numActa);

        } catch (Exception e) {
            log.error("[NOTIF] Error crítico en proceso de notificación de acta {}: {}",
                    numActa, e.getMessage(), e);
        }
    }

    private List<String> resolverDestinatarios(SiiActaVisitaEntity actaVisita, SiiAutoComisorioEntity auto) {
        Set<String> emails = new HashSet<>();

        // 1. Email del representante presente en la visita
        if (actaVisita != null && StringUtils.hasText(actaVisita.getAviEmailPresente())) {
            emails.add(actaVisita.getAviEmailPresente().trim());
        }

        // 2. Correos adicionales de contacto registrados en el acta
        if (actaVisita != null && StringUtils.hasText(actaVisita.getAviCorreosContacto())) {
            String[] contactos = actaVisita.getAviCorreosContacto().split("[,;]");
            for (String c : contactos) {
                if (StringUtils.hasText(c)) {
                    emails.add(c.trim());
                }
            }
        }

        // 3. Email oficial del operador (desde el contrato)
        if (auto.getSiiContrato() != null &&
                auto.getSiiContrato().getSiiOperadorEntity() != null &&
                auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona() != null) {

            String emailOperador = auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerEmail();
            if (StringUtils.hasText(emailOperador)) {
                emails.add(emailOperador.trim());
            }

            String emailAlterno = auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerEmailAlterno();
            if (StringUtils.hasText(emailAlterno)) {
                emails.add(emailAlterno.trim());
            }
        }

        // Filtrar solo los que cumplen con el patrón de email válido
        return emails.stream()
                .filter(e -> e.matches(EMAIL_PATTERN))
                .collect(Collectors.toList());
    }

    private void enviarCorreo(List<String> destinatarios, byte[] pdf, Integer numActa) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setFrom(remitente);
            helper.setReplyTo(remitente);
            helper.setSubject("Acta de Visita de Fiscalización No. " + numActa);

            String htmlContent = "<h3>Notificación de Acta de Visita</h3>" +
                    "<p>Adjunto encontrará el acta de la visita de fiscalización realizada.</p>" +
                    "<p>Favor no responder a este correo.</p>";

            helper.setText(htmlContent, true);

            // Adjuntar el PDF
            helper.addAttachment("ActaVisita_" + numActa + ".pdf", new ByteArrayResource(pdf));

            mailSender.send(message);
            log.info("[NOTIF] Correo enviado exitosamente para acta {}", numActa);

        } catch (Exception e) {
            log.error("[NOTIF] Error al enviar correo para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error en el envío de correo", e);
        }
    }

}
