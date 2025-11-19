package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.ObtenerActaResponseDTO;

public interface ActaService {

    ObtenerActaResponseDTO obtenerActas(Long perCodigo);

}
