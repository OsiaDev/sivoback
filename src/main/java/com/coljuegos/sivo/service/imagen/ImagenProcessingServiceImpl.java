package com.coljuegos.sivo.service.imagen;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Slf4j
@Service
public class ImagenProcessingServiceImpl implements ImagenProcessingService {

    /**
     * Tamaño máximo permitido para imágenes: 15 MB
     */
    private static final int MAX_IMAGE_SIZE = 15 * 1024 * 1024;

    /**
     * Tamaño del buffer para descompresión
     */
    private static final int BUFFER_SIZE = 4096;

    @Override
    public byte[] descomprimirImagen(String base64Compressed) throws IOException, DataFormatException {
        if (base64Compressed == null || base64Compressed.trim().isEmpty()) {
            throw new IllegalArgumentException("La imagen base64 no puede ser nula o vacía");
        }

        log.debug("Iniciando descompresión de imagen. Longitud base64: {}", base64Compressed.length());

        byte[] imagenDescomprimida = null;
        Inflater inflater = null;

        try {
            // Decodificar base64 para obtener datos comprimidos
            byte[] compressed = Base64.decodeBase64(base64Compressed);
            log.debug("Datos comprimidos decodificados. Tamaño: {} bytes", compressed.length);

            // Inicializar inflater para descompresión ZLIB
            inflater = new Inflater();
            inflater.setInput(compressed);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int totalBytes = 0;

            // Descomprimir datos
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);

                // Verificar condiciones especiales
                if (count == 0) {
                    if (inflater.needsInput()) {
                        log.debug("Inflater necesita más entrada - finalizando descompresión");
                        break;
                    }
                    if (inflater.needsDictionary()) {
                        log.error("ZLIB requiere diccionario - no soportado");
                        throw new DataFormatException("ZLIB dictionary not supported");
                    }
                }

                // Verificar límite de tamaño
                totalBytes += count;
                if (totalBytes > MAX_IMAGE_SIZE) {
                    log.error("Imagen excede tamaño máximo permitido. Total: {} bytes, Máximo: {} bytes",
                            totalBytes, MAX_IMAGE_SIZE);
                    throw new IOException("Imagen excede tamaño máximo permitido de " +
                            (MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
                }

                // Escribir bytes descomprimidos
                out.write(buffer, 0, count);
            }

            imagenDescomprimida = out.toByteArray();

            log.info("Imagen descomprimida exitosamente. Tamaño original: {} bytes, Tamaño descomprimido: {} bytes",
                    compressed.length, imagenDescomprimida.length);

            return imagenDescomprimida;

        } catch (DataFormatException e) {
            log.error("Error en formato de datos durante descompresión: {}", e.getMessage(), e);
            throw new DataFormatException("Formato de compresión inválido: " + e.getMessage());

        } catch (IOException e) {
            log.error("Error de I/O durante descompresión: {}", e.getMessage(), e);
            throw new IOException("Error al descomprimir imagen: " + e.getMessage());

        } finally {
            // Liberar recursos del inflater
            if (inflater != null) {
                inflater.end();
                log.debug("Recursos del inflater liberados");
            }
        }
    }

}