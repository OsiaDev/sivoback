package com.coljuegos.sivo.service.imagen;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface ImagenProcessingService {

    /**
     * Descomprime una imagen codificada en base64 con compresión ZLIB
     *
     * @param base64Compressed Imagen comprimida en formato base64
     * @return Array de bytes de la imagen descomprimida
     * @throws IOException Si hay error de I/O o la imagen excede el tamaño máximo
     * @throws DataFormatException Si el formato de compresión es inválido
     */
    byte[] descomprimirImagen(String base64Compressed) throws IOException, DataFormatException;

}