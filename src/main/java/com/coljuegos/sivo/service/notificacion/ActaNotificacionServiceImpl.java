package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import com.coljuegos.sivo.service.reporte.ActaReporteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActaNotificacionServiceImpl implements ActaNotificacionService {

    private final ActaReporteService actaReporteService;
    private final ActaReporteContextMapper actaReporteContextMapper;
    private final JavaMailSender mailSender;
    private final ActaNotificacionProperties notificacionProperties;

    @Override
    public void notificarActaAsync(SiiAutoComisorioEntity autoComisorio,
                                   SiiActaVisitaEntity actaVisita,
                                   SiiVerificacionContractualEntity contractual,
                                   SiiVerificacionSiplaftEntity siplaft,
                                   SiiVerificacionJuegoResponsableEntity juegoResponsableEntity,
                                   SiiFirmaActaEntity firma,
                                   List<SiiInventarioRegistradoEntity> inventarios,
                                   List<SiiNovedadRegistradaEntity> novedades) {
        Integer numActa = autoComisorio.getAucNumero();
        log.info("[NOTIF] Iniciando notificación asíncrona para acta {}", numActa);

        try {
            // 1. Construir contexto y generar PDF una sola vez en memoria
            ActaReporteContextDTO context = actaReporteContextMapper.mapear(
                    autoComisorio, actaVisita, contractual, siplaft, firma, inventarios, novedades);

            byte[] pdf = actaReporteService.generarReporteActa(context);

            if (pdf == null || pdf.length == 0) {
                log.error("[NOTIF] No se pudo generar el PDF para acta {}. Se cancela el envío.", numActa);
                return;
            }

            // 2. Resolver destinatarios
            List<String> destinatarios = resolverDestinatarios(actaVisita, autoComisorio.getAucCodigo());

            if (destinatarios.isEmpty()) {
                log.warn("[NOTIF] No hay destinatarios válidos para acta {}. Se omite el envío.", numActa);
                return;
            }

            log.info("[NOTIF] Enviando acta {} a {} destinatario(s): {}",
                    numActa, destinatarios.size(), destinatarios);

            // 3. Enviar uno a uno
            String nombreArchivo = "ActaVisita_" + numActa + ".pdf";

            for (String correo : destinatarios) {
                enviarCorreo(correo, pdf, nombreArchivo, numActa);
            }

            log.info("[NOTIF] Notificación completada para acta {}", numActa);

        } catch (Exception e) {
            log.error("[NOTIF] Error general en notificación de acta {}: {}",
                    numActa, e.getMessage(), e);
        }
    }

}
