package com.coljuegos.sivo.service.firma;

import com.coljuegos.sivo.data.dto.acta.FirmaActaDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiFirmaActaEntity;

/**
 * Servicio para gestionar el almacenamiento de firmas digitales de actas
 */
public interface FirmaActaStorageService {

    /**
     * Guarda las firmas del acta, descomprimiendo las imágenes base64
     *
     * @param firmaActaDTO Datos de las firmas
     * @param autoComisorio Auto comisorio asociado
     * @param numActa Número del acta
     * @return Entidad guardada
     */
    SiiFirmaActaEntity guardarFirmasActa(
            FirmaActaDTO firmaActaDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Elimina firmas de un acta (útil para actualizaciones)
     *
     * @param numActa Número del acta
     * @return true si se eliminó, false si no existía
     */
    boolean eliminarFirmasDeActa(Integer numActa);

}