package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;

public interface UploadActaService {

    ActaSincronizacionResponseDTO procesarActaSubida(ActaCompleteDTO actaCompleteDTO, Long perCodigo);

}
