package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.ObtenerActaResponseDTO;
import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;

public interface ActaService {

    ObtenerActaResponseDTO obtenerActas(Long perCodigo);

    ActaSincronizacionResponseDTO procesarActaSubida(ActaCompleteDTO actaCompleteDTO, Long perCodigo);

}
