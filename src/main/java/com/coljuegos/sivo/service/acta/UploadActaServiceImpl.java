package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.acta.*;
import com.coljuegos.sivo.data.entity.EstadoVisita;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.SiiGrupoFiscalizacionEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import com.coljuegos.sivo.data.repository.*;
import com.coljuegos.sivo.service.firma.FirmaActaStorageService;
import com.coljuegos.sivo.service.imagen.ImagenStorageService;
import com.coljuegos.sivo.service.inventario.InventarioBingoRegistradoStorageService;
import com.coljuegos.sivo.service.inventario.InventarioRegistradoStorageService;
import com.coljuegos.sivo.service.novedad.NovedadRegistradaStorageService;
import com.coljuegos.sivo.service.notificacion.ActaNotificacionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UploadActaServiceImpl implements UploadActaService {

    private final AutoComisorioRepository autoComisorioRepository;

    private final ActaVisitaRepository actaVisitaRepository;

    private final VerificacionContractualRepository verificacionContractualRepository;

    private final VerificacionSiplaftRepository verificacionSiplaftRepository;

    private final VerificacionJuegoResponsableRepository verificacionJuegoResponsableRepository;

    private final ImagenStorageService imagenStorageService;

    private final InventarioRegistradoStorageService inventarioRegistradoStorageService;

    private final NovedadRegistradaStorageService novedadRegistradaStorageService;

    private final FirmaActaStorageService firmaActaStorageService;

    private final ResumenInventarioRepository resumenInventarioRepository;
    
    private final VerificacionBingoRepository verificacionBingoRepository;
    
    private final InventarioBingoRegistradoStorageService inventarioBingoRegistradoStorageService;

    private final ActaNotificacionService actaNotificacionService;

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

            // --- RECOPILACION DE ENTIDADES PARA NOTIFICACION ASINCRONA ---
            SiiActaVisitaEntity entidadActa = null;
            SiiVerificacionContractualEntity entidadContractual = null;
            SiiVerificacionSiplaftEntity entidadSiplaft = null;
            SiiVerificacionJuegoResponsableEntity entidadJuegoResp = null;
            SiiFirmaActaEntity entidadFirma = null;
            SiiResumenInventarioEntity entidadResumen = null;
            SiiVerificacionBingoEntity entidadBingo = null;
            List<SiiInventarioRegistradoEntity> listaInventarios = Collections.emptyList();
            List<SiiInventarioBingoRegistradoEntity> listaInventariosBingo = Collections.emptyList();
            List<SiiNovedadRegistradaEntity> listaNovedades = Collections.emptyList();

            // Guardar la información de ActaVisitaDTO
            if (actaCompleteDTO.getActaVisita() != null) {
                entidadActa = this.guardarActaVisita(actaCompleteDTO.getActaVisita(), autoComisorio, actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionContractualDTO
            if (actaCompleteDTO.getVerificacionContractual() != null) {
                entidadContractual = this.guardarVerificacionContractual(
                        actaCompleteDTO.getVerificacionContractual(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionSiplaftDTO
            if (actaCompleteDTO.getVerificacionSiplaft() != null) {
                entidadSiplaft = this.guardarVerificacionSiplaft(
                        actaCompleteDTO.getVerificacionSiplaft(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionJuegoResponsableDTO
            if (actaCompleteDTO.getVerificacionJuegoResponsable() != null) {
                entidadJuegoResp = this.guardarVerificacionJuegoResponsable(
                        actaCompleteDTO.getVerificacionJuegoResponsable(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar inventarios registrados
            if (actaCompleteDTO.getInventariosRegistrados() != null
                    && !actaCompleteDTO.getInventariosRegistrados().isEmpty()) {
                listaInventarios = this.guardarInventarios(
                        actaCompleteDTO.getInventariosRegistrados(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar la información de VerificacionBingoDTO
            if (actaCompleteDTO.getVerificacionBingo() != null) {
                entidadBingo = this.guardarVerificacionBingo(
                        actaCompleteDTO.getVerificacionBingo(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar inventarios registrados de bingo
            if (actaCompleteDTO.getInventariosBingoRegistrados() != null
                    && !actaCompleteDTO.getInventariosBingoRegistrados().isEmpty()) {
                listaInventariosBingo = this.guardarInventariosBingo(
                        actaCompleteDTO.getInventariosBingoRegistrados(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar novedades registradas
            if (actaCompleteDTO.getNovedadesRegistradas() != null
                    && !actaCompleteDTO.getNovedadesRegistradas().isEmpty()) {
                listaNovedades = this.guardarNovedades(
                        actaCompleteDTO.getNovedadesRegistradas(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }


            // Guardar las firmas del acta
            if (actaCompleteDTO.getFirmaActa() != null) {
                entidadFirma = this.guardarFirmasActa(
                        actaCompleteDTO.getFirmaActa(),
                        autoComisorio,
                        actaCompleteDTO.getNumActa());
            }

            // Guardar resumen del inventario (incluye coordenadas GPS del acta)
            entidadResumen = this.guardarResumenInventario(
                    actaCompleteDTO.getResumenInventario(),
                    actaCompleteDTO.getLatitud(),
                    actaCompleteDTO.getLongitud(),
                    autoComisorio,
                    actaCompleteDTO.getNumActa());

            // Cambiar el estado de visita a VISITADO
            EstadoVisita estadoAnterior = autoComisorio.getEstadoVisita();
            autoComisorio.setEstadoVisita(EstadoVisita.VISITADO);
            autoComisorio.setAucFechaVisitaRealizada(LocalDateTime.now());

            // Guardar los cambios
            this.autoComisorioRepository.save(autoComisorio);

            log.info("Acta {} actualizada exitosamente. Estado anterior: {}, Estado nuevo: VISITADO",
                    actaCompleteDTO.getNumActa(), estadoAnterior);

            // 5. Lanzar proceso de reporte y notificación asíncrona
            // PASAMOS TODAS LAS ENTIDADES DIRECTAMENTE PARA EVITAR HANGS POR RE-QUERY
            this.lanzarNotificacionAsincronaDirecto(
                    autoComisorio, entidadActa, entidadContractual, entidadSiplaft, 
                    entidadJuegoResp, entidadFirma, entidadResumen, listaInventarios, listaNovedades,
                    entidadBingo, listaInventariosBingo);

            return ActaSincronizacionResponseDTO.success(
                    actaCompleteDTO.getNumActa(),
                    "Acta procesada exitonamente");

        } catch (Exception e) {
            log.error("Error al procesar subida de acta: {}", e.getMessage(), e);
            return ActaSincronizacionResponseDTO.error(
                    "Error al procesar el acta: " + e.getMessage());
        }
    }

    /**
     * Prepara y lanza la notificación asíncrona usando las entidades ya cargadas en memoria.
     */
    private void lanzarNotificacionAsincronaDirecto(
            SiiAutoComisorioEntity auto,
            SiiActaVisitaEntity acta,
            SiiVerificacionContractualEntity contractual,
            SiiVerificacionSiplaftEntity siplaft,
            SiiVerificacionJuegoResponsableEntity juegoResp,
            SiiFirmaActaEntity firma,
            SiiResumenInventarioEntity resumen,
            List<SiiInventarioRegistradoEntity> inventarios,
            List<SiiNovedadRegistradaEntity> novedades,
            SiiVerificacionBingoEntity verificacionBingo,
            List<SiiInventarioBingoRegistradoEntity> inventariosBingo) {
        
        try {
            log.info("[ASYNC-TRIGGER] Disparando notificación directa para acta {} (Sin consultas extras)", auto.getAucNumero());

            // Inicializar asociaciones de carga perezosa que NO están en las entidades de arriba
            // (Como Operador, Persona, etc. que cuelgan del AutoComisorio)
            this.inicializarAsociaciones(auto);

            actaNotificacionService.notificarActaAsync(
                    auto, acta, contractual, siplaft, juegoResp, firma, resumen, inventarios, novedades,
                    verificacionBingo, inventariosBingo
            );

        } catch (Exception e) {
            log.error("[ASYNC-TRIGGER] Falló el disparo de la notificación asíncrona para acta {}: {}", 
                    auto.getAucNumero(), e.getMessage(), e);
        }
    }

    /**
     * "Toca" las asociaciones de carga perezosa para inicializarlas dentro de la sesión actual (Hibernate).
     */
    private void inicializarAsociaciones(SiiAutoComisorioEntity auto) {
        if (auto.getSiiContrato() != null) {
            auto.getSiiContrato().getConNumero(); // Touch proxy
            if (auto.getSiiContrato().getSiiOperadorEntity() != null) {
                auto.getSiiContrato().getSiiOperadorEntity().getOpeCodigo(); // Touch proxy
                if (auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona() != null) {
                    auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerNumIdentificacion(); // Touch proxy
                }
            }
        }
        if (auto.getSiiEstablecimiento() != null) {
            auto.getSiiEstablecimiento().getEstNombre(); // Touch proxy
            if (auto.getSiiEstablecimiento().getSiiUbicacion() != null) {
                auto.getSiiEstablecimiento().getSiiUbicacion().getUbiNombre(); // Touch proxy
                if (auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre() != null) {
                    auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre().getUbiNombre(); // Touch proxy
                }
            }
        }
    }

    @Override
    @Transactional
    public ActaSincronizacionResponseDTO procesarSubidaImagenIndividual(UploadImagenActaDTO dto, Long perCodigo) {
        try {
            log.info("Procesando subida de imagen individual para acta número: {}", dto.getNumActa());

            if (dto.getNumActa() == null || dto.getImagen() == null) {
                log.warn("Datos de acta inválidos o número de acta nulo para la imagen aislada");
                return ActaSincronizacionResponseDTO.error("Datos inválidos, acta o imagen faltante");
            }

            // Buscar el auto comisorio por número de acta
            Optional<SiiAutoComisorioEntity> autoOpt = this.autoComisorioRepository
                    .findByAucNumero(dto.getNumActa());

            if (autoOpt.isEmpty()) {
                log.warn("No se encontró el auto comisorio con número: {}", dto.getNumActa());
                return ActaSincronizacionResponseDTO.error(
                        "No se encontró el acta con número: " + dto.getNumActa());
            }

            SiiAutoComisorioEntity autoComisorio = autoOpt.get();

            // Verificar que el auto comisorio pertenezca al fiscalizador
            if (!perteneceAFiscalizador(autoComisorio, perCodigo)) {
                log.warn("El fiscalizador {} no tiene permiso para subir imagenes al acta {}",
                        perCodigo, dto.getNumActa());
                return ActaSincronizacionResponseDTO.error(
                        "No tiene permisos para modificar esta acta");
            }

            // Delegar a ImagenStorageService con la logica Idempotente
            this.imagenStorageService.guardarImagenIndividual(dto.getImagen(), autoComisorio, dto.getNumActa());

            return ActaSincronizacionResponseDTO.success(
                    dto.getNumActa(),
                    "Imagen procesada exitosamente");

        } catch (Exception e) {
            log.error("Error al procesar subida individual de imagen: {}", e.getMessage(), e);
            return ActaSincronizacionResponseDTO.error(
                    "Error al procesar imagen: " + e.getMessage());
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

    private SiiActaVisitaEntity guardarActaVisita(ActaVisitaDTO actaVisitaDTO, SiiAutoComisorioEntity autoComisorio, Integer numActa) {
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
            actaVisitaEntity.setAviTipoDocumentoPresente(actaVisitaDTO.getTipoDocumentoPresente());

            // Guardar en la base de datos
            SiiActaVisitaEntity guardado = this.actaVisitaRepository.save(actaVisitaEntity);

            log.info("ActaVisita guardada exitosamente para acta: {}, código: {}",
                    numActa, guardado.getAviCodigo());
            return guardado;

        } catch (Exception e) {
            log.error("Error al guardar ActaVisita para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar información de visita: " + e.getMessage(), e);
        }
    }

    private SiiVerificacionContractualEntity guardarVerificacionContractual(VerificacionContractualDTO verificacionDTO,
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
            SiiVerificacionContractualEntity guardado = this.verificacionContractualRepository.save(verificacionEntity);

            log.info("VerificacionContractual guardada exitosamente para acta: {}, código: {}",
                    numActa, guardado.getVcoCodigo());
            return guardado;

        } catch (Exception e) {
            log.error("Error al guardar VerificacionContractual para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación contractual: " + e.getMessage(), e);
        }
    }

    private SiiVerificacionSiplaftEntity guardarVerificacionSiplaft(VerificacionSiplaftDTO verificacionDTO,
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
            SiiVerificacionSiplaftEntity guardado = this.verificacionSiplaftRepository.save(verificacionEntity);

            log.info("VerificacionSiplaft guardada exitosamente para acta: {}, código: {}",
                    numActa, guardado.getVsiCodigo());
            return guardado;

        } catch (Exception e) {
            log.error("Error al guardar VerificacionSiplaft para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación SIPLAFT: " + e.getMessage(), e);
        }
    }

    private SiiVerificacionJuegoResponsableEntity guardarVerificacionJuegoResponsable(VerificacionJuegoResponsableDTO verificacionDTO,
                                                     SiiAutoComisorioEntity autoComisorio,
                                                     Integer numActa) {
        try {
            log.info("Guardando información de VerificacionJuegoResponsable para acta número: {}", numActa);

            Optional<SiiVerificacionJuegoResponsableEntity> verificacionExistente =
                    this.verificacionJuegoResponsableRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiVerificacionJuegoResponsableEntity verificacionEntity;

            if (verificacionExistente.isPresent()) {
                log.info("Actualizando registro existente de VerificacionJuegoResponsable para acta: {}", numActa);
                verificacionEntity = verificacionExistente.get();
            } else {
                log.info("Creando nuevo registro de VerificacionJuegoResponsable para acta: {}", numActa);
                verificacionEntity = new SiiVerificacionJuegoResponsableEntity();
                verificacionEntity.setSiiAutoComisorio(autoComisorio);
                verificacionEntity.setVjrNumActa(numActa);
                verificacionEntity.setVjrFechaRegistro(LocalDateTime.now());
            }

            verificacionEntity.setVjrCuentaTestIdentRiesgos(verificacionDTO.getCuentaTestIdentificacionRiesgos());
            verificacionEntity.setVjrExistenPiezasPublicitarias(verificacionDTO.getExistenPiezasPublicitarias());
            verificacionEntity.setVjrCuentaProgramaJuegoResp(verificacionDTO.getCuentaProgramaJuegoResponsable());

            SiiVerificacionJuegoResponsableEntity guardado = this.verificacionJuegoResponsableRepository.save(verificacionEntity);

            log.info("VerificacionJuegoResponsable guardada exitosamente para acta: {}, código: {}",
                    numActa, guardado.getVjrCodigo());
            return guardado;

        } catch (Exception e) {
            log.error("Error al guardar VerificacionJuegoResponsable para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación de juego responsable: " + e.getMessage(), e);
        }
    }

    private List<SiiInventarioRegistradoEntity> guardarInventarios(List<InventarioRegistradoDTO> inventarios,
                                    SiiAutoComisorioEntity autoComisorio,
                                    Integer numActa) {
        try {
            if (inventarios == null || inventarios.isEmpty()) {
                log.debug("No hay inventarios para guardar en acta {}", numActa);
                return Collections.emptyList();
            }

            log.info("Iniciando guardado de {} inventarios para acta {}", inventarios.size(), numActa);

            List<SiiInventarioRegistradoEntity> inventariosGuardados = this.inventarioRegistradoStorageService.guardarInventarios(
                    inventarios,
                    autoComisorio,
                    numActa);

            log.info("Se guardaron exitosamente {}/{} inventarios para acta {}",
                    inventariosGuardados.size(), inventarios.size(), numActa);

            return inventariosGuardados;

        } catch (Exception e) {
            log.error("Error al guardar inventarios del acta {}: {}. Se continuará con el resto del proceso.",
                    numActa, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<SiiInventarioBingoRegistradoEntity> guardarInventariosBingo(List<InventarioBingoRegistradoDTO> inventarios,
                                     SiiAutoComisorioEntity autoComisorio,
                                     Integer numActa) {
        try {
            if (inventarios == null || inventarios.isEmpty()) {
                return Collections.emptyList();
            }

            return this.inventarioBingoRegistradoStorageService.guardarInventarios(
                    inventarios,
                    autoComisorio,
                    numActa);

        } catch (Exception e) {
            log.error("Error al guardar inventarios de bingo del acta {}: {}. Se continuará con el resto del proceso.",
                    numActa, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private SiiVerificacionBingoEntity guardarVerificacionBingo(VerificacionBingoDTO verificacionDTO,
                                                    SiiAutoComisorioEntity autoComisorio,
                                                    Integer numActa) {
        try {
            log.info("Guardando información de VerificacionBingo para acta número: {}", numActa);

            Optional<SiiVerificacionBingoEntity> verificacionExistente =
                    this.verificacionBingoRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiVerificacionBingoEntity verificacionEntity;

            if (verificacionExistente.isPresent()) {
                log.info("Actualizando registro existente de VerificacionBingo para acta: {}", numActa);
                verificacionEntity = verificacionExistente.get();
            } else {
                log.info("Creando nuevo registro de VerificacionBingo para acta: {}", numActa);
                verificacionEntity = new SiiVerificacionBingoEntity();
                verificacionEntity.setSiiAutoComisorio(autoComisorio);
                verificacionEntity.setVbiNumActa(numActa);
                verificacionEntity.setVbiFechaRegistro(LocalDateTime.now());
            }

            verificacionEntity.setVbiCartonesModulos(verificacionDTO.getCartonesModulos());
            verificacionEntity.setVbiSistemaTecnologico(verificacionDTO.getSistemaTecnologico());
            verificacionEntity.setVbiSistemaInterconectado(verificacionDTO.getSistemaInterconectado());
            verificacionEntity.setVbiEventosEspeciales(verificacionDTO.getRealizaEventosEspeciales());
            verificacionEntity.setVbiTipoBalotera(verificacionDTO.getTipoBalotera());
            verificacionEntity.setVbiValorCarton(verificacionDTO.getValorCartonExpuesto());

            return this.verificacionBingoRepository.save(verificacionEntity);

        } catch (Exception e) {
            log.error("Error al guardar VerificacionBingo para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar verificación BINGO: " + e.getMessage(), e);
        }
    }

    private List<SiiNovedadRegistradaEntity> guardarNovedades(List<NovedadRegistradaDTO> novedades,
                                  SiiAutoComisorioEntity autoComisorio,
                                  Integer numActa) {
        try {
            if (novedades == null || novedades.isEmpty()) {
                log.debug("No hay novedades para guardar en acta {}", numActa);
                return Collections.emptyList();
            }

            log.info("Iniciando guardado de {} novedades para acta {}", novedades.size(), numActa);

            List<SiiNovedadRegistradaEntity> novedadesGuardadas = this.novedadRegistradaStorageService.guardarNovedades(
                    novedades,
                    autoComisorio,
                    numActa);

            log.info("Se guardaron exitosamente {}/{} novedades para acta {}",
                    novedadesGuardadas.size(), novedades.size(), numActa);

            return novedadesGuardadas;

        } catch (Exception e) {
            log.error("Error al guardar novedades del acta {}: {}. Se continuará con el resto del proceso.",
                    numActa, e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    private SiiFirmaActaEntity guardarFirmasActa(FirmaActaDTO firmaActaDTO,
                                   SiiAutoComisorioEntity autoComisorio,
                                   Integer numActa) {
        try {
            if (firmaActaDTO == null) {
                log.debug("No hay firmas para guardar en acta {}", numActa);
                return null;
            }

            log.info("Iniciando guardado de firmas para acta {}", numActa);

            SiiFirmaActaEntity firmaGuardada = this.firmaActaStorageService.guardarFirmasActa(
                    firmaActaDTO,
                    autoComisorio,
                    numActa);

            log.info("Firmas guardadas exitosamente para acta {}, código: {}",
                    numActa, firmaGuardada.getFiaCodigo());
            return firmaGuardada;

        } catch (Exception e) {
            log.error("Error al guardar firmas del acta {}: {}. Se continuará con el resto del proceso.",
                    numActa, e.getMessage(), e);
            return null;
        }
    }

    private SiiResumenInventarioEntity guardarResumenInventario(ResumenInventarioDTO resumenDTO,
                                          Double latitud,
                                          Double longitud,
                                          SiiAutoComisorioEntity autoComisorio,
                                          Integer numActa) {
        try {
            log.info("Guardando ResumenInventario para acta número: {}", numActa);

            Optional<SiiResumenInventarioEntity> resumenExistente =
                    this.resumenInventarioRepository.findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

            SiiResumenInventarioEntity resumenEntity;

            if (resumenExistente.isPresent()) {
                log.info("Actualizando registro existente de ResumenInventario para acta: {}", numActa);
                resumenEntity = resumenExistente.get();
            } else {
                log.info("Creando nuevo registro de ResumenInventario para acta: {}", numActa);
                resumenEntity = new SiiResumenInventarioEntity();
                resumenEntity.setSiiAutoComisorio(autoComisorio);
                resumenEntity.setRsiNumActa(numActa);
                resumenEntity.setRsiFechaRegistro(LocalDateTime.now());
            }

            // Notas del resumen (puede venir null si el DTO es null)
            resumenEntity.setRsiNotasResumen(resumenDTO != null ? resumenDTO.getNotasResumen() : null);
            resumenEntity.setRsiObservacionesOperador(resumenDTO != null ? resumenDTO.getObservacionesOperador() : null);

            // Coordenadas GPS tomadas del raíz del ActaCompleteDTO
            resumenEntity.setRsiLatitud(latitud);
            resumenEntity.setRsiLongitud(longitud);

            this.resumenInventarioRepository.save(resumenEntity);

            log.info("ResumenInventario guardado exitosamente para acta: {}, código: {}",
                    numActa, resumenEntity.getRsiCodigo());
            return resumenEntity;

        } catch (Exception e) {
            log.error("Error al guardar ResumenInventario para acta {}: {}", numActa, e.getMessage(), e);
            throw new RuntimeException("Error al guardar resumen de inventario: " + e.getMessage(), e);
        }
    }

}
