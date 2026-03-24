package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.entity.visita.SiiActaVisitaEntity;
import com.coljuegos.sivo.data.repository.ActaVisitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActaNotificacionScheduler {

    private final ActaVisitaRepository actaVisitaRepository;
    private final ReenvioCorreoService reenvioCorreoService;

    @Scheduled(fixedDelayString = "${acta.notificacion.retry.delay:300000}")
    @Transactional
    public void procesarReintentos() {
        log.debug("[SCHEDULER] Buscando actas pendientes o con error de notificación para reintento...");
        List<SiiActaVisitaEntity> actasParaNotificar = actaVisitaRepository.findActasParaNotificar(5);

        if (actasParaNotificar == null || actasParaNotificar.isEmpty()) {
            return;
        }

        log.info("[SCHEDULER] Se encontraron {} actas para procesar reintento.", actasParaNotificar.size());

        for (SiiActaVisitaEntity acta : actasParaNotificar) {
            try {
                reenvioCorreoService.reenviarCorreoPost(acta.getSiiAutoComisorio().getAucNumero());
            } catch (Exception e) {
                log.error("[SCHEDULER] Error al invocar el reenvío de correo para el acta {}: {}",
                        acta.getSiiAutoComisorio().getAucNumero(), e.getMessage());
            }
        }
    }

}
