package com.coljuegos.sivo.service.inventario;

import com.coljuegos.sivo.data.dto.acta.InventarioRegistradoDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiInventarioRegistradoEntity;

import java.util.Collection;
import java.util.List;

/**
 * Servicio para gestionar el almacenamiento de inventarios registrados durante visitas
 */
public interface InventarioRegistradoStorageService {

    /**
     * Guarda una colección de inventarios registrados durante una visita
     *
     * @param inventarios Colección de inventarios a guardar
     * @param autoComisorio Auto comisorio asociado a la visita
     * @param numActa Número del acta de la visita
     * @return Lista de entidades guardadas
     */
    List<SiiInventarioRegistradoEntity> guardarInventarios(
            Collection<InventarioRegistradoDTO> inventarios,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Guarda un inventario registrado individual
     *
     * @param inventarioDTO Datos del inventario
     * @param autoComisorio Auto comisorio asociado
     * @param numActa Número del acta
     * @return Entidad guardada
     */
    SiiInventarioRegistradoEntity guardarInventario(
            InventarioRegistradoDTO inventarioDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Elimina inventarios registrados de un acta (útil para actualizaciones)
     *
     * @param numActa Número del acta
     * @return Cantidad de inventarios eliminados
     */
    int eliminarInventariosDeActa(Integer numActa);

}