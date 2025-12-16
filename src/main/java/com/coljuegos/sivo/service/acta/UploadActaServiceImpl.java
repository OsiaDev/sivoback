package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.acta.*;
import com.coljuegos.sivo.data.entity.EstadoVisita;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.SiiGrupoFiscalizacionEntity;
import com.coljuegos.sivo.data.entity.visita.SiiActaVisitaEntity;
import com.coljuegos.sivo.data.entity.visita.SiiImagenActaEntity;
import com.coljuegos.sivo.data.entity.visita.SiiVerificacionContractualEntity;
import com.coljuegos.sivo.data.entity.visita.SiiVerificacionSiplaftEntity;
import com.coljuegos.sivo.data.repository.ActaVisitaRepository;
import com.coljuegos.sivo.data.repository.AutoComisorioRepository;
import com.coljuegos.sivo.data.repository.VerificacionContractualRepository;
import com.coljuegos.sivo.data.repository.VerificacionSiplaftRepository;
import com.coljuegos.sivo.service.imagen.ImagenStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Slf4j
@Service
@AllArgsConstructor
public class UploadActaServiceImpl implements UploadActaService {

    private final AutoComisorioRepository autoComisorioRepository;

    private final ActaVisitaRepository actaVisitaRepository;

    private final VerificacionContractualRepository verificacionContractualRepository;

    private final VerificacionSiplaftRepository verificacionSiplaftRepository;

    private final ImagenStorageService imagenStorageService;

    @Override
    @Transactional
    public ActaSincronizacionResponseDTO procesarActaSubida(ActaCompleteDTO actaCompleteDTO, Long perCodigo) {
        try {
            log.info("Procesando subida de acta número: {}", actaCompleteDTO.getNumActa());

            if (actaCompleteDTO.getNumActa() == null) {
                log.warn("Datos de acta inválidos o número de acta nulo");
                return ActaSincronizacionResponseDTO.error("Datos de acta inválidos");
            }

            // Buscar el auto comisorio por número de actas
            Optional<SiiAutoComisorioEntity> autoOpt = this.autoComisorioRepository
                    .findByAucNumero(actaCompleteDTO.getNumActa());

            if (autoOpt.isEmpty()) {
                log.warn("No se encontró el auto comisorio con número: {}", actaCompleteDTO.getNumActa());
                return ActaSincronizacionResponseDTO.error(
                        "No se encontró el acta con número: " + actaCompleteDTO.getNumActa());
            }

            SiiAutoComisorioEntity autoComisorio = autoOpt.get();

            // Verificar que el auto comisorio pertenezca al fiscalizador
            if (!perteneceAFiscalizador(autoComisorio, perCodigo)) {
                log.warn("El fiscalizador {} no tiene permiso para modificar el acta {}",
                        perCodigo, actaCompleteDTO.getNumActa());
                return ActaSincronizacionResponseDTO.error(
                        "No tiene permisos para modificar esta acta");
            }

            // Guardar la información de ActaVisitaDTO
            if (actaCompleteDTO.getActaVisita() != null) {
                this.guardarActaVisita(actaCompleteDTO.getActaVisita(), autoComisorio, actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionContractualDTO
            if (actaCompleteDTO.getVerificacionContractual() != null) {
                this.guardarVerificacionContractual(
                        actaCompleteDTO.getVerificacionContractual(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionSiplaftDTO
            if (actaCompleteDTO.getVerificacionSiplaft() != null) {
                this.guardarVerificacionSiplaft(
                        actaCompleteDTO.getVerificacionSiplaft(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar las imágenes del acta
            if (actaCompleteDTO.getImagenes() != null && !actaCompleteDTO.getImagenes().isEmpty()) {
                this.guardarImagenes(
                        actaCompleteDTO.getImagenes(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Cambiar el estado de visita a VISITADO
            EstadoVisita estadoAnterior = autoComisorio.getEstadoVisita();
            autoComisorio.setEstadoVisita(EstadoVisita.VISITADO);
            autoComisorio.setAucFechaVisitaRealizada(LocalDateTime.now());

            // Guardar los cambios
            this.autoComisorioRepository.save(autoComisorio);

            log.info("Acta {} actualizada exitosamente. Estado anterior: {}, Estado nuevo: VISITADO",
                    actaCompleteDTO.getNumActa(), estadoAnterior);

            return ActaSincronizacionResponseDTO.success(
                    actaCompleteDTO.getNumActa(),
                    "Acta procesada exitosamente");

        } catch (Exception e) {
            log.error("Error al procesar subida de acta: {}", e.getMessage(), e);
            return ActaSincronizacionResponseDTO.error(
                    "Error al procesar el acta: " + e.getMessage());
        }
    }

    private boolean perteneceAFiscalizador(SiiAutoComisorioEntity autoComisorio, Long perCodigo) {
        if (autoComisorio.getSiiGrupoFiscalizacion() == null) {
            return false;
        }

        SiiGrupoFiscalizacionEntity grupo = autoComisorio.getSiiGrupoFiscalizacion();

        // Verificar fiscalizador principal
        if (grupo.getFsuCodigoPrincip() != null &&
                grupo.getFsuCodigoPrincip().getSiiPersona() != null &&
                perCodigo.equals(grupo.getFsuCodigoPrincip().getSiiPersona().getPerCodigo())) {
            return true;
        }

        // Verificar fiscalizador acompañante
        return grupo.getFsuCodigoAcomp() != null &&
                grupo.getFsuCodigoAcomp().getSiiPersona() != null &&
                perCodigo.equals(grupo.getFsuCodigoAcomp().getSiiPersona().getPerCodigo());
    }

    private void guardarActaVisita(ActaVisitaDTO actaVisitaDTO, SiiAutoComisorioEntity autoComisorio, Integer numActa) {
        try {
            log.info("Guardando información de ActaVisita para acta número: {}", numActa);

            // Verificar si ya existe un registro para este auto comisorio
            Optional<SiiActaVisitaEntity> actaExistente = this.actaVisitaRepository
                    .findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiActaVisitaEntity actaVisitaEntity;

            if (actaExistente.isPresent()) {
                // Actualizar registro existente
                log.info("Actualizando registro existente de ActaVisita para acta: {}", numActa);
                actaVisitaEntity = actaExistente.get();
            } else {
                // Crear nuevo registro
                log.info("Creando nuevo registro de ActaVisita para acta: {}", numActa);
                actaVisitaEntity = new SiiActaVisitaEntity();
                actaVisitaEntity.setSiiAutoComisorio(autoComisorio);
                actaVisitaEntity.setAviNumActa(numActa);
                actaVisitaEntity.setAviFechaRegistro(LocalDateTime.now());
            }

            // Mapear datos del DTO a la entidad
            actaVisitaEntity.setAviNombrePresente(actaVisitaDTO.getNombrePresente());
            actaVisitaEntity.setAviIdentificacionPresente(actaVisitaDTO.getIdentificacionPresente());
            actaVisitaEntity.setAviMunicipio(actaVisitaDTO.getMunicipio());
            actaVisitaEntity.setAviCargoPresente(actaVisitaDTO.getCargoPresente());
            actaVisitaEntity.setAviEmailPresente(actaVisitaDTO.getEmailPresente());
            actaVisitaEntity.setAviCorreosContacto(actaVisitaDTO.getCorreosContacto());

            // Guardar en la base de datos
            this.actaVisitaRepository.save(actaVisitaEntity);

            log.info("ActaVisita guardada exitosamente para acta: {}, código: {}",
                    numActa, actaVisitaEntity.getAviCodigo());

        } catch (Exception e) {
            log.error("Error al guardar ActaVisita para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar información de visita: " + e.getMessage(), e);
        }
    }

    private void guardarVerificacionContractual(VerificacionContractualDTO verificacionDTO,
                                                SiiAutoComisorioEntity autoComisorio,
                                                Integer numActa) {
        try {
            log.info("Guardando información de VerificacionContractual para acta número: {}", numActa);

            // Verificar si ya existe un registro para este auto comisorio
            Optional<SiiVerificacionContractualEntity> verificacionExistente =
                    this.verificacionContractualRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiVerificacionContractualEntity verificacionEntity;

            if (verificacionExistente.isPresent()) {
                // Actualizar registro existente
                log.info("Actualizando registro existente de VerificacionContractual para acta: {}", numActa);
                verificacionEntity = verificacionExistente.get();
            } else {
                // Crear nuevo registro
                log.info("Creando nuevo registro de VerificacionContractual para acta: {}", numActa);
                verificacionEntity = new SiiVerificacionContractualEntity();
                verificacionEntity.setSiiAutoComisorio(autoComisorio);
                verificacionEntity.setVcoNumActa(numActa);
                verificacionEntity.setVcoFechaRegistro(LocalDateTime.now());
            }

            // Mapear datos del DTO a la entidad
            verificacionEntity.setVcoAvisoAutorizacion(verificacionDTO.getAvisoAutorizacion());
            verificacionEntity.setVcoDireccionCorresponde(verificacionDTO.getDireccionCorresponde());
            verificacionEntity.setVcoOtraDireccion(verificacionDTO.getOtraDireccion());
            verificacionEntity.setVcoNombreEstCorresponde(verificacionDTO.getNombreEstablecimientoCorresponde());
            verificacionEntity.setVcoOtroNombre(verificacionDTO.getOtroNombre());
            verificacionEntity.setVcoActividadesDiferentes(verificacionDTO.getDesarrollaActividadesDiferentes());
            verificacionEntity.setVcoTipoActividad(verificacionDTO.getTipoActividad());
            verificacionEntity.setVcoEspecificacionOtros(verificacionDTO.getEspecificacionOtros());
            verificacionEntity.setVcoRegistrosMantenimiento(verificacionDTO.getCuentaRegistrosMantenimiento());

            // Guardar en la base de datos
            this.verificacionContractualRepository.save(verificacionEntity);

            log.info("VerificacionContractual guardada exitosamente para acta: {}, código: {}",
                    numActa, verificacionEntity.getVcoCodigo());

        } catch (Exception e) {
            log.error("Error al guardar VerificacionContractual para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación contractual: " + e.getMessage(), e);
        }
    }

    private void guardarVerificacionSiplaft(VerificacionSiplaftDTO verificacionDTO,
                                            SiiAutoComisorioEntity autoComisorio,
                                            Integer numActa) {
        try {
            log.info("Guardando información de VerificacionSiplaft para acta número: {}", numActa);

            // Verificar si ya existe un registro para este auto comisorio
            Optional<SiiVerificacionSiplaftEntity> verificacionExistente =
                    this.verificacionSiplaftRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiVerificacionSiplaftEntity verificacionEntity;

            if (verificacionExistente.isPresent()) {
                // Actualizar registro existente
                log.info("Actualizando registro existente de VerificacionSiplaft para acta: {}", numActa);
                verificacionEntity = verificacionExistente.get();
            } else {
                // Crear nuevo registro
                log.info("Creando nuevo registro de VerificacionSiplaft para acta: {}", numActa);
                verificacionEntity = new SiiVerificacionSiplaftEntity();
                verificacionEntity.setSiiAutoComisorio(autoComisorio);
                verificacionEntity.setVsiNumActa(numActa);
                verificacionEntity.setVsiFechaRegistro(LocalDateTime.now());
            }

            // Mapear datos del DTO a la entidad
            verificacionEntity.setVsiFormatoIdentificacion(verificacionDTO.getCuentaFormatoIdentificacion());
            verificacionEntity.setVsiMontoIdentificacion(verificacionDTO.getMontoIdentificacion());
            verificacionEntity.setVsiFormatoReporteInterno(verificacionDTO.getCuentaFormatoReporteInterno());
            verificacionEntity.setVsiSenalesAlerta(verificacionDTO.getSenalesAlerta());
            verificacionEntity.setVsiConoceCodigoConducta(verificacionDTO.getConoceCodigoConducta());

            // Guardar en la base de datos
            this.verificacionSiplaftRepository.save(verificacionEntity);

            log.info("VerificacionSiplaft guardada exitosamente para acta: {}, código: {}",
                    numActa, verificacionEntity.getVsiCodigo());

        } catch (Exception e) {
            log.error("Error al guardar VerificacionSiplaft para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación SIPLAFT: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda las imágenes del acta
     */
    private void guardarImagenes(List<ImagenDTO> imagenes,
                                 SiiAutoComisorioEntity autoComisorio,
                                 Integer numActa) {
        try {
            if (imagenes == null || imagenes.isEmpty()) {
                log.debug("No hay imágenes para guardar en acta {}", numActa);
                return;
            }

            log.info("Iniciando guardado de {} imágenes para acta {}", imagenes.size(), numActa);

            // Guardar las imágenes usando el servicio de almacenamiento
            List<SiiImagenActaEntity> imagenesGuardadas = this.imagenStorageService.guardarImagenes(
                    imagenes,
                    autoComisorio,
                    numActa);

            log.info("Se guardaron exitosamente {}/{} imágenes para acta {}",
                    imagenesGuardadas.size(), imagenes.size(), numActa);

            // Si no se guardaron todas las imágenes, registrar advertencia
            if (imagenesGuardadas.size() < imagenes.size()) {
                log.warn("Solo se guardaron {}/{} imágenes para acta {}. Revisar logs para detalles.",
                        imagenesGuardadas.size(), imagenes.size(), numActa);
            }

        } catch (Exception e) {
            // Log del error pero no lanzar excepción para no fallar toda la transacción
            // Las imágenes son importante pero no críticas
            log.error("Error al guardar imágenes del acta {}: {}. Se continuará con el resto del proceso.",
                    numActa, e.getMessage(), e);
        }
    }

}
