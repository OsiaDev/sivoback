package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;

import java.util.List;

public interface ActaNotificacionService {

    /**
     * Genera el PDF del acta y lo envía uno a uno a cada destinatario,
     * de forma asíncrona. Recibe las entidades ya cargadas en memoria
     * desde el flujo de procesarActaSubida.
     */
    void notificarActaAsync(SiiAutoComisorioEntity autoComisorio,
                            SiiActaVisitaEntity actaVisita,
                            SiiVerificacionContractualEntity contractual,
                            SiiVerificacionSiplaftEntity siplaft,
                            SiiVerificacionJuegoResponsableEntity juegoResponsableEntity,
                            SiiFirmaActaEntity firma,
                            SiiResumenInventarioEntity resumen,
                            List<SiiInventarioRegistradoEntity> inventarios,
                            List<SiiNovedadRegistradaEntity> novedades,
                            SiiVerificacionBingoEntity verificacionBingo,
                            List<SiiInventarioBingoRegistradoEntity> inventariosBingo);

}