package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;
import com.coljuegos.sivo.data.dto.acta.UploadImagenActaDTO;

public interface UploadActaService {

    /**
     * Procesa la subida y sincronización de listados e información de un Acta de
     * Visita.
     *
     * @param actaCompleteDTO Información completa del acta desde el móvil
     * @param perCodigo       Código del usuario (fiscalizador) que realiza la
     *                        subida
     * @return DTO con el resultado de la operación
     */
    ActaSincronizacionResponseDTO procesarActaSubida(ActaCompleteDTO actaCompleteDTO, Long perCodigo);

    /**
     * Procesa la subida de una imagen individual previniendo duplicidad.
     */
    ActaSincronizacionResponseDTO procesarSubidaImagenIndividual(UploadImagenActaDTO dto, Long perCodigo);

}
