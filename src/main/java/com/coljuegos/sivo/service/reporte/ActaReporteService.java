package com.coljuegos.sivo.service.reporte;

import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;

public interface ActaReporteService {

    /**
     * Genera el reporte PDF del acta de visita de forma síncrona.
     * Retorna los bytes del PDF generado.
     *
     * @param context todos los datos necesarios para construir el reporte
     * @return byte[] con el PDF, o null si ocurrió un error
     */
    byte[] generarReporteActa(ActaReporteContextDTO context);

}