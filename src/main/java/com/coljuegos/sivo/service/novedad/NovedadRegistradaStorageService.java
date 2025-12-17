package com.coljuegos.sivo.service.novedad;

import com.coljuegos.sivo.data.dto.acta.NovedadRegistradaDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiNovedadRegistradaEntity;

import java.util.Collection;
import java.util.List;

/**
 * Servicio para gestionar el almacenamiento de novedades registradas durante visitas
 */
public interface NovedadRegistradaStorageService {

    /**
     * Guarda una colección de novedades registradas durante una visita
     *
     * @param novedades Colección de novedades a guardar
     * @param autoComisorio Auto comisorio asociado a la visita
     * @param numActa Número del acta de la visita
     * @return Lista de entidades guardadas
     */
    List<SiiNovedadRegistradaEntity> guardarNovedades(
            Collection<NovedadRegistradaDTO> novedades,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Guarda una novedad registrada individual
     *
     * @param novedadDTO Datos de la novedad
     * @param autoComisorio Auto comisorio asociado
     * @param numActa Número del acta
     * @return Entidad guardada
     */
    SiiNovedadRegistradaEntity guardarNovedad(
            NovedadRegistradaDTO novedadDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Elimina novedades registradas de un acta (útil para actualizaciones)
     *
     * @param numActa Número del acta
     * @return Cantidad de novedades eliminadas
     */
    int eliminarNovedadesDeActa(Integer numActa);

}