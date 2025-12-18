package com.coljuegos.sivo.service.firma;

import com.coljuegos.sivo.data.dto.acta.FirmaActaDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiFirmaActaEntity;
import com.coljuegos.sivo.data.repository.FirmaActaRepository;
import com.coljuegos.sivo.service.imagen.ImagenProcessingService;
import lombok.AllArgsConstructor;
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
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;

@Slf4j
@Service
public class FirmaActaStorageServiceImpl implements FirmaActaStorageService {

    private final FirmaActaRepository firmaActaRepository;
    private final ImagenProcessingService imagenProcessingService;

    @Value("${acta.imagenes.base-path:D:/archivoSiicol}")
    private String baseImagePath;

    @Value("${acta.imagenes.relative-path:/sivo/actas}")
    private String relativePath;

    @Value("${acta.imagenes.max-size:15728640}")
    private long maxImageSize;

    public FirmaActaStorageServiceImpl(FirmaActaRepository firmaActaRepository,
                                       ImagenProcessingService imagenProcessingService) {
        this.firmaActaRepository = firmaActaRepository;
        this.imagenProcessingService = imagenProcessingService;
    }

    @Override
    @Transactional
    public SiiFirmaActaEntity guardarFirmasActa(
            FirmaActaDTO firmaActaDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) {

        if (firmaActaDTO == null) {
            throw new IllegalArgumentException("FirmaActaDTO no puede ser null");
        }

        log.info("Guardando firmas del acta {}", numActa);

        try {
            // Verificar si ya existe un registro para este auto comisorio
            Optional<SiiFirmaActaEntity> firmaExistente =
                    this.firmaActaRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiFirmaActaEntity firmaEntity;

            if (firmaExistente.isPresent()) {
                // Actualizar registro existente
                log.info("Actualizando registro existente de firmas para acta: {}", numActa);
                firmaEntity = firmaExistente.get();

                // Eliminar firmas antiguas del sistema de archivos si existen
                eliminarFirmaFisica(firmaEntity.getFiaPathFirmaFiscPrincipal());
                eliminarFirmaFisica(firmaEntity.getFiaPathFirmaFiscSecundario());
                eliminarFirmaFisica(firmaEntity.getFiaPathFirmaOperador());
            } else {
                // Crear nuevo registro
                log.info("Creando nuevo registro de firmas para acta: {}", numActa);
                firmaEntity = new SiiFirmaActaEntity();
                firmaEntity.setSiiAutoComisorio(autoComisorio);
                firmaEntity.setFiaNumActa(numActa);
            }

            // Generar directorio base para las firmas
            String pathRelativo = generarPathRelativoFirmas(numActa);

            // Procesar firma del fiscalizador principal
            if (firmaActaDTO.getFirmaFiscalizadorPrincipal() != null &&
                    !firmaActaDTO.getFirmaFiscalizadorPrincipal().trim().isEmpty()) {

                String pathFirmaPrincipal = procesarYGuardarFirma(
                        firmaActaDTO.getFirmaFiscalizadorPrincipal(),
                        pathRelativo,
                        "firma_fisc_principal");

                firmaEntity.setFiaNombreFiscPrincipal(firmaActaDTO.getNombreFiscalizadorPrincipal());
                firmaEntity.setFiaCcFiscPrincipal(firmaActaDTO.getCcFiscalizadorPrincipal());
                firmaEntity.setFiaCargoFiscPrincipal(firmaActaDTO.getCargoFiscalizadorPrincipal());
                firmaEntity.setFiaPathFirmaFiscPrincipal(pathFirmaPrincipal);
            }

            // Procesar firma del fiscalizador secundario
            if (firmaActaDTO.getFirmaFiscalizadorSecundario() != null &&
                    !firmaActaDTO.getFirmaFiscalizadorSecundario().trim().isEmpty()) {

                String pathFirmaSecundario = procesarYGuardarFirma(
                        firmaActaDTO.getFirmaFiscalizadorSecundario(),
                        pathRelativo,
                        "firma_fisc_secundario");

                firmaEntity.setFiaNombreFiscSecundario(firmaActaDTO.getNombreFiscalizadorSecundario());
                firmaEntity.setFiaCcFiscSecundario(firmaActaDTO.getCcFiscalizadorSecundario());
                firmaEntity.setFiaCargoFiscSecundario(firmaActaDTO.getCargoFiscalizadorSecundario());
                firmaEntity.setFiaPathFirmaFiscSecundario(pathFirmaSecundario);
            }

            // Procesar firma del operador
            if (firmaActaDTO.getFirmaOperador() != null &&
                    !firmaActaDTO.getFirmaOperador().trim().isEmpty()) {

                String pathFirmaOperador = procesarYGuardarFirma(
                        firmaActaDTO.getFirmaOperador(),
                        pathRelativo,
                        "firma_operador");

                firmaEntity.setFiaNombreOperador(firmaActaDTO.getNombreOperador());
                firmaEntity.setFiaCcOperador(firmaActaDTO.getCcOperador());
                firmaEntity.setFiaCargoOperador(firmaActaDTO.getCargoOperador());
                firmaEntity.setFiaPathFirmaOperador(pathFirmaOperador);
            }

            // Guardar en la base de datos
            SiiFirmaActaEntity firmaGuardada = this.firmaActaRepository.save(firmaEntity);

            log.info("Firmas del acta {} guardadas exitosamente. Código: {}",
                    numActa, firmaGuardada.getFiaCodigo());

            return firmaGuardada;

        } catch (Exception e) {
            log.error("Error al guardar firmas del acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar firmas del acta: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean eliminarFirmasDeActa(Integer numActa) {
        try {
            log.info("Eliminando firmas del acta {}", numActa);

            Optional<SiiFirmaActaEntity> firmaExistente =
                    this.firmaActaRepository.findByNumActa(numActa);

            if (firmaExistente.isEmpty()) {
                log.debug("No hay firmas para eliminar del acta {}", numActa);
                return false;
            }

            SiiFirmaActaEntity firma = firmaExistente.get();

            // Eliminar archivos físicos
            eliminarFirmaFisica(firma.getFiaPathFirmaFiscPrincipal());
            eliminarFirmaFisica(firma.getFiaPathFirmaFiscSecundario());
            eliminarFirmaFisica(firma.getFiaPathFirmaOperador());

            // Eliminar registro de BD
            this.firmaActaRepository.delete(firma);

            log.info("Firmas del acta {} eliminadas exitosamente", numActa);
            return true;

        } catch (Exception e) {
            log.error("Error al eliminar firmas del acta {}: {}", numActa, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Procesa y guarda una firma: descomprime base64 y guarda en disco
     *
     * @param firmaBase64Comprimida Firma en base64 comprimida con ZLIB
     * @param pathRelativo Path relativo donde guardar la firma
     * @param prefijo Prefijo para el nombre del archivo
     * @return Path relativo del archivo guardado
     */
    private String procesarYGuardarFirma(
            String firmaBase64Comprimida,
            String pathRelativo,
            String prefijo) throws IOException, DataFormatException {

        log.debug("Procesando firma con prefijo: {}", prefijo);

        // 1. Descomprimir la firma
        byte[] firmaDescomprimida = this.imagenProcessingService.descomprimirImagen(
                firmaBase64Comprimida);

        log.debug("Firma '{}' descomprimida: {} bytes", prefijo, firmaDescomprimida.length);

        // 2. Validar tamaño
        if (firmaDescomprimida.length > maxImageSize) {
            throw new IOException(
                    String.format("Firma '%s' excede tamaño máximo: %d bytes (máximo: %d bytes)",
                            prefijo,
                            firmaDescomprimida.length,
                            maxImageSize));
        }

        // 3. Generar nombre de archivo único
        String nombreArchivo = generarNombreArchivoFirma(prefijo);

        // 4. Guardar físicamente la firma
        Path archivoPath = guardarFirmaFisica(
                firmaDescomprimida,
                pathRelativo,
                nombreArchivo);

        log.info("Firma guardada físicamente: {}", archivoPath);

        // 5. Retornar path relativo para almacenar en BD
        return pathRelativo + "/" + nombreArchivo;
    }

    /**
     * Genera el path relativo para almacenar las firmas del acta
     *
     * @param numActa Número del acta
     * @return Path relativo en formato: /sivo/actas/YYYY/MM/DD/acta_NNNNNN/firmas
     */
    private String generarPathRelativoFirmas(Integer numActa) {
        LocalDate ahora = LocalDate.now();

        // Formato de fecha: YYYY/MM/DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String fechaPath = ahora.format(formatter);

        // Formato final: /sivo/actas/2024/12/18/acta_000123/firmas
        return String.format("%s/%s/acta_%06d/firmas", relativePath, fechaPath, numActa);
    }

    /**
     * Genera un nombre único para el archivo de firma
     *
     * @param prefijo Prefijo identificador (ej: "firma_fisc_principal")
     * @return Nombre del archivo con timestamp y UUID
     */
    private String generarNombreArchivoFirma(String prefijo) {
        // Timestamp en formato: yyyyMMddHHmmss
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // UUID corto (primeros 8 caracteres)
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        // Formato: firma_fisc_principal_20241218143025_a1b2c3d4.png
        return String.format("%s_%s_%s.png", prefijo, timestamp, uuid);
    }

    /**
     * Guarda físicamente una firma en el sistema de archivos
     *
     * @param firmaBytes Bytes de la firma
     * @param pathRelativo Path relativo del directorio
     * @param nombreArchivo Nombre del archivo
     * @return Path completo del archivo guardado
     */
    private Path guardarFirmaFisica(
            byte[] firmaBytes,
            String pathRelativo,
            String nombreArchivo) throws IOException {

        // Construir ruta completa del directorio
        Path directorioCompleto = Paths.get(baseImagePath, pathRelativo);

        // Crear directorios si no existen
        if (!Files.exists(directorioCompleto)) {
            Files.createDirectories(directorioCompleto);
            log.debug("Directorios creados para firma: {}", directorioCompleto);
        }

        // Ruta completa del archivo
        Path archivoPath = directorioCompleto.resolve(nombreArchivo);

        // Escribir el archivo
        Files.write(archivoPath, firmaBytes,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        log.debug("Archivo de firma escrito: {} ({} bytes)", archivoPath, firmaBytes.length);

        return archivoPath;
    }

    /**
     * Elimina un archivo de firma del sistema de archivos
     *
     * @param pathRelativo Path relativo del archivo a eliminar
     */
    private void eliminarFirmaFisica(String pathRelativo) {
        if (pathRelativo == null || pathRelativo.trim().isEmpty()) {
            return;
        }

        try {
            Path archivoPath = Paths.get(baseImagePath, pathRelativo);

            if (Files.exists(archivoPath)) {
                Files.delete(archivoPath);
                log.debug("Archivo de firma eliminado: {}", archivoPath);
            } else {
                log.warn("Archivo de firma no encontrado para eliminar: {}", archivoPath);
            }

        } catch (IOException e) {
            log.error("Error al eliminar archivo de firma {}: {}",
                    pathRelativo, e.getMessage(), e);
        }
    }

}