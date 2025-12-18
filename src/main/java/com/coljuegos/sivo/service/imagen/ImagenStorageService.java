package com.coljuegos.sivo.service.imagen;

import com.coljuegos.sivo.data.dto.acta.ImagenDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiImagenActaEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Servicio para gestionar el almacenamiento físico y registro en BD de imágenes de actas
 */
public interface ImagenStorageService {

    /**
     * Guarda una colección de imágenes asociadas a un acta
     *
     * @param imagenes Lista de imágenes a guardar
     * @param autoComisorio Auto comisorio asociado
     * @param numActa Número del acta
     * @return Lista de entidades guardadas
     */
    List<SiiImagenActaEntity> guardarImagenes(
            Collection<ImagenDTO> imagenes,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa);

    /**
     * Guarda una sola imagen
     *
     * @param imagenDTO Datos de la imagen
     * @param autoComisorio Auto comisorio asociado
     * @param numActa Número del acta
     * @return Entidad guardada
     * @throws IOException Si hay error al guardar la imagen físicamente
     * @throws DataFormatException Si hay error en el formato de compresión
     */
    SiiImagenActaEntity guardarImagen(
            ImagenDTO imagenDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) throws IOException, DataFormatException;

    /**
     * Elimina imágenes antiguas de un acta (útil para actualización)
     *
     * @param numActa Número del acta
     * @return Cantidad de imágenes eliminadas
     */
    int eliminarImagenesDeActa(Integer numActa);

}