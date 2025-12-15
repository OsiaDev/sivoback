package com.coljuegos.sivo.service.imagen;

import com.coljuegos.sivo.data.dto.acta.ImagenDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiImagenActaEntity;
import com.coljuegos.sivo.data.repository.ImagenActaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;

@Slf4j
@Service
public class ImagenStorageServiceImpl implements ImagenStorageService {

    private final ImagenActaRepository imagenActaRepository;

    private final ImagenProcessingService imagenProcessingService;

    @Value("${acta.imagenes.base-path:${user.home}/sivo-imagenes}")
    private String baseImagePath;

    @Value("${acta.imagenes.max-size:15728640}")
    private long maxImageSize;

    public ImagenStorageServiceImpl(
            ImagenActaRepository imagenActaRepository,
            ImagenProcessingService imagenProcessingService) {
        this.imagenActaRepository = imagenActaRepository;
        this.imagenProcessingService = imagenProcessingService;
    }

    @Override
    @Transactional
    public List<SiiImagenActaEntity> guardarImagenes(
            Collection<ImagenDTO> imagenes,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) throws IOException, DataFormatException {

        if (imagenes == null || imagenes.isEmpty()) {
            log.debug("No hay imágenes para guardar en acta {}", numActa);
            return new ArrayList<>();
        }

        log.info("Guardando {} imágenes para acta {}", imagenes.size(), numActa);

        List<SiiImagenActaEntity> imagenesGuardadas = new ArrayList<>();
        int contador = 0;

        for (ImagenDTO imagenDTO : imagenes) {
            try {
                contador++;
                log.debug("Procesando imagen {}/{} para acta {}",
                        contador, imagenes.size(), numActa);

                SiiImagenActaEntity imagenGuardada = guardarImagen(
                        imagenDTO,
                        autoComisorio,
                        numActa);

                imagenesGuardadas.add(imagenGuardada);

            } catch (Exception e) {
                log.error("Error al guardar imagen {}/{} del acta {}: {}",
                        contador, imagenes.size(), numActa, e.getMessage(), e);
                // Continuar con las demás imágenes en lugar de fallar completamente
            }
        }

        log.info("Se guardaron exitosamente {}/{} imágenes para acta {}",
                imagenesGuardadas.size(), imagenes.size(), numActa);

        return imagenesGuardadas;
    }

    @Override
    @Transactional
    public SiiImagenActaEntity guardarImagen(
            ImagenDTO imagenDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) throws IOException, DataFormatException {

        if (imagenDTO == null) {
            throw new IllegalArgumentException("ImagenDTO no puede ser null");
        }

        if (imagenDTO.getImagenBase64() == null || imagenDTO.getImagenBase64().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Imagen '" + imagenDTO.getNombreImagen() + "' no tiene datos base64");
        }

        log.debug("Procesando imagen: {}", imagenDTO.getNombreImagen());

        // 1. Descomprimir la imagen
        byte[] imagenDescomprimida = this.imagenProcessingService.descomprimirImagen(
                imagenDTO.getImagenBase64());

        log.debug("Imagen '{}' descomprimida: {} bytes",
                imagenDTO.getNombreImagen(), imagenDescomprimida.length);

        // 2. Validar tamaño
        if (imagenDescomprimida.length > maxImageSize) {
            throw new IOException(
                    String.format("Imagen '%s' excede tamaño máximo: %d bytes (máximo: %d bytes)",
                            imagenDTO.getNombreImagen(),
                            imagenDescomprimida.length,
                            maxImageSize));
        }

        // 3. Generar estructura de directorios y nombre de archivo
        String pathRelativo = generarPathRelativo(numActa);
        String nombreArchivoFisico = generarNombreArchivoUnico(imagenDTO.getNombreImagen());

        // 4. Guardar físicamente la imagen
        Path archivoPath = guardarImagenFisica(
                imagenDescomprimida,
                pathRelativo,
                nombreArchivoFisico);

        log.info("Imagen guardada físicamente: {}", archivoPath);

        // 5. Crear y guardar registro en BD
        SiiImagenActaEntity imagenEntity = new SiiImagenActaEntity();
        imagenEntity.setSiiAutoComisorio(autoComisorio);
        imagenEntity.setImaNumActa(numActa);
        imagenEntity.setImaNombreImagen(imagenDTO.getNombreImagen());
        imagenEntity.setImaPathArchivo(pathRelativo + "/" + nombreArchivoFisico);
        imagenEntity.setImaDescripcion(imagenDTO.getDescripcion());
        imagenEntity.setImaFragmentoOrigen(imagenDTO.getFragmentOrigen());
        imagenEntity.setImaTamanioBytes((long) imagenDescomprimida.length);
        imagenEntity.setImaTipoMime(detectarTipoMime(imagenDTO.getNombreImagen()));

        SiiImagenActaEntity imagenGuardada = this.imagenActaRepository.save(imagenEntity);

        log.info("Registro de imagen creado en BD: código={}, acta={}, archivo={}",
                imagenGuardada.getImaCodigo(), numActa, imagenGuardada.getImaPathArchivo());

        return imagenGuardada;
    }

    @Override
    @Transactional
    public int eliminarImagenesDeActa(Integer numActa) {
        try {
            log.info("Eliminando imágenes existentes del acta {}", numActa);

            Collection<SiiImagenActaEntity> imagenesExistentes =
                    this.imagenActaRepository.findByNumActa(numActa);

            if (imagenesExistentes.isEmpty()) {
                log.debug("No hay imágenes existentes para eliminar del acta {}", numActa);
                return 0;
            }

            int eliminadas = 0;

            for (SiiImagenActaEntity imagen : imagenesExistentes) {
                try {
                    // Eliminar archivo físico
                    eliminarArchivoFisico(imagen.getImaPathArchivo());

                    // Eliminar registro de BD
                    this.imagenActaRepository.delete(imagen);

                    eliminadas++;

                } catch (Exception e) {
                    log.error("Error al eliminar imagen {}: {}",
                            imagen.getImaCodigo(), e.getMessage(), e);
                }
            }

            log.info("Se eliminaron {} imágenes del acta {}", eliminadas, numActa);
            return eliminadas;

        } catch (Exception e) {
            log.error("Error al eliminar imágenes del acta {}: {}", numActa, e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Genera la estructura de directorios para organizar las imágenes por fecha y acta
     * Formato: YYYY/MM/DD/acta_NNNNNN
     */
    private String generarPathRelativo(Integer numActa) {
        LocalDate ahora = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String fechaPath = ahora.format(formatter);

        return String.format("%s/acta_%06d", fechaPath, numActa);
    }

    /**
     * Genera un nombre único para el archivo físico preservando la extensión original
     */
    private String generarNombreArchivoUnico(String nombreOriginal) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // Extraer extensión del nombre original
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        return String.format("%s_%s%s", timestamp, uuid, extension);
    }

    /**
     * Guarda físicamente la imagen en el sistema de archivos
     */
    private Path guardarImagenFisica(
            byte[] imagenBytes,
            String pathRelativo,
            String nombreArchivo) throws IOException {

        // Construir ruta completa
        Path directorioCompleto = Paths.get(baseImagePath, pathRelativo);

        // Crear directorios si no existen
        if (!Files.exists(directorioCompleto)) {
            Files.createDirectories(directorioCompleto);
            log.debug("Directorios creados: {}", directorioCompleto);
        }

        // Ruta completa del archivo
        Path archivoPath = directorioCompleto.resolve(nombreArchivo);

        // Validar que el archivo no exista (por seguridad)
        if (Files.exists(archivoPath)) {
            log.warn("El archivo {} ya existe, se sobrescribirá", archivoPath);
        }

        // Escribir el archivo
        Files.write(archivoPath, imagenBytes,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        log.debug("Archivo escrito: {} ({} bytes)", archivoPath, imagenBytes.length);

        return archivoPath;
    }

    /**
     * Elimina un archivo físico del sistema
     */
    private void eliminarArchivoFisico(String pathRelativo) {
        try {
            Path archivoPath = Paths.get(baseImagePath, pathRelativo);

            if (Files.exists(archivoPath)) {
                Files.delete(archivoPath);
                log.debug("Archivo eliminado: {}", archivoPath);
            } else {
                log.warn("Archivo no encontrado para eliminar: {}", archivoPath);
            }

        } catch (IOException e) {
            log.error("Error al eliminar archivo físico {}: {}",
                    pathRelativo, e.getMessage(), e);
        }
    }

    /**
     * Detecta el tipo MIME basándose en la extensión del archivo
     */
    private String detectarTipoMime(String nombreArchivo) {
        if (nombreArchivo == null) {
            return "application/octet-stream";
        }

        String nombreLower = nombreArchivo.toLowerCase();

        if (nombreLower.endsWith(".jpg") || nombreLower.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (nombreLower.endsWith(".png")) {
            return "image/png";
        } else if (nombreLower.endsWith(".gif")) {
            return "image/gif";
        } else if (nombreLower.endsWith(".webp")) {
            return "image/webp";
        } else if (nombreLower.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            return "application/octet-stream";
        }
    }

}