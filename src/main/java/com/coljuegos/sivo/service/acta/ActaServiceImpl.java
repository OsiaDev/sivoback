package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.*;
import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;
import com.coljuegos.sivo.data.entity.*;
import com.coljuegos.sivo.data.repository.AutoComisorioRepository;
import com.coljuegos.sivo.data.repository.InventarioRepository;
import com.coljuegos.sivo.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ActaServiceImpl implements ActaService {

    private final static String ACTIVO = "A";

    private final static String VISITA_F = "F";

    private final static String ONLINE = "S";

    private final AutoComisorioRepository autoComisorioRepository;

    private final InventarioRepository inventarioRepository;

    @Override
    public ObtenerActaResponseDTO obtenerActas(Long perCodigo) {
        ObtenerActaResponseDTO response = new ObtenerActaResponseDTO();
        Collection<SiiAutoComisorioEntity> autos = this.autoComisFiscaPrinciVisita(perCodigo);
        if (autos.isEmpty()) {
            response.setActas(Collections.emptyList());
            return response;
        }

        response.setActas(this.procesarAutoComisorio(autos));
        return response;
    }

    @Override
    @Transactional
    public ActaSincronizacionResponseDTO procesarActaSubida(ActaCompleteDTO actaCompleteDTO, Long perCodigo) {
        try {
            log.info("Procesando subida de acta número: {}", actaCompleteDTO.getNumActa());

            if (actaCompleteDTO.getNumActa() == null) {
                log.warn("Datos de acta inválidos o número de acta nulo");
                return ActaSincronizacionResponseDTO.error("Datos de acta inválidos");
            }

            // Buscar el auto comisorio por número de acta
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

            // Cambiar el estado de visita a VISITADO
            EstadoVisita estadoAnterior = autoComisorio.getEstadoVisita();
            autoComisorio.setEstadoVisita(EstadoVisita.VISITADO);

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

    public Collection<SiiAutoComisorioEntity> autoComisFiscaPrinciVisita(Long perCodigo) throws CustomException {
        try {
            LocalDate fechaActual = LocalDate.now();

            // 360 horas ≈ 15 días atrás
            LocalDate fechaMesAtras = fechaActual.minusDays(15);

            return this.autoComisorioRepository.findFiltradosPorPersonaEstadoTipoYFechas(
                    perCodigo,
                    ACTIVO,
                    fechaMesAtras,
                    fechaActual
            );

        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general de base de datos: " + e.getMessage());
        }
    }

    private Collection<ActaDTO> procesarAutoComisorio(Collection<SiiAutoComisorioEntity> siiAutoComisorios) {
        Collection<ActaDTO> actas = new ArrayList<>();
        if (!siiAutoComisorios.isEmpty()) {

            actas.addAll(siiAutoComisorios.stream().map(this::mapAuto).toList());

            Collection<SiiAutoComisorioEntity> actasFiltradas = siiAutoComisorios.stream()
                    .filter(a -> VISITA_F.equals(a.getAucTipoVisita()))
                    .toList();

            Collection<Integer> aucNumeros = this.groupAucNumeros(actasFiltradas);

            Collection<Long> conCodigos = this.groupConCodigos(actasFiltradas);

            Collection<Long> estCodigos = this.groupEstCodigos(actasFiltradas);

            Collection<InventarioProjection> inventariosRaw = this.inventarioRepository.buscarInventariosPorFiltros(
                    conCodigos, aucNumeros, estCodigos);

            Map<String, List<InventarioDTO>> inventarioMap = this.groupInventarios(inventariosRaw);
            for (ActaDTO acta : actas) {
                String codigo = acta.getConCodigo() + "|" + acta.getNumAuc() + "|" + acta.getEstCodigo();
                Collection<InventarioDTO> inventario = inventarioMap.get(codigo);
                acta.setInventarios((inventario != null && !inventario.isEmpty()) ? inventario : Collections.emptyList());
            }
        }

        return actas;
    }

    private Map<String, List<InventarioDTO>> groupInventarios(Collection<InventarioProjection> inventarios) {
        return inventarios.stream().map(this::mapInventario).collect(Collectors.groupingBy(dto ->
                dto.getConCodigo() + "|" + dto.getAucNumero() + "|" + dto.getEstCodigo()
        ));
    }

    private Collection<Long> groupConCodigos(Collection<SiiAutoComisorioEntity> actasFiltradas) {
        return actasFiltradas.stream()
                .map(ac -> ac.getSiiContrato().getConCodigo())
                .distinct().toList();
    }

    private Collection<Long> groupEstCodigos(Collection<SiiAutoComisorioEntity> actasFiltradas) {
        return actasFiltradas.stream()
                .map(ac -> ac.getSiiEstablecimiento().getEstCodigo())
                .distinct().toList();
    }

    private Collection<Integer> groupAucNumeros(Collection<SiiAutoComisorioEntity> actasFiltradas) {
        return actasFiltradas.stream()
                .map(SiiAutoComisorioEntity::getAucNumero)
                .distinct().toList();
    }

    private InventarioDTO mapInventario(InventarioProjection inventario) {
        return InventarioDTO.builder()
                .nuc(inventario.getMetUid())
                .metSerial(inventario.getMetSerial())
                .metOnline(ONLINE.equals(inventario.getMetOnline()))
                .invSillas(Optional.ofNullable(inventario.getInvSillas()).orElse(0))
                .codigoTipoApuesta(inventario.getTipoApuestaCodigo())
                .nombreMarca(inventario.getMarcaNombre())
                .tipoApuestaNombre(inventario.getTipoApuestaNombre())
                .insCodigo(inventario.getInstrumentoCodigo())
                .conCodigo(inventario.getConCodigo())
                .aucNumero(inventario.getAucNumero())
                .estCodigo(inventario.getEstCodigo())
                .build();
    }

    private ActaDTO mapAuto(SiiAutoComisorioEntity auto) {
        if (auto == null) return null;

        SiiContratoEntity contrato = auto.getSiiContrato();
        SiiEstablecimientoEntity establecimiento = auto.getSiiEstablecimiento();

        String numContrato = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getConNumero)
                .orElse(null);

        Long conCodigo = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getConCodigo)
                .orElse(null);

        String estCodigoInterno = Optional.ofNullable(establecimiento)
                .map(SiiEstablecimientoEntity::getEstCodInterno)
                .orElse(null);

        Long estCodigo = Optional.ofNullable(establecimiento)
                .map(SiiEstablecimientoEntity::getEstCodigo)
                .orElse(null);

        String identificacion = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getSiiOperadorEntity)
                .map(SiiOperadorEntity::getSiiPersona)
                .map(SiiPersonaEntity::getPerNumIdentificacion)
                .orElse(null);

        String nombreOperador = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getSiiOperadorEntity)
                .map(SiiOperadorEntity::getSiiPersona)
                .map(SiiPersonaEntity::getPerJurNombreLargo)
                .orElse("");

        String email = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getSiiOperadorEntity)
                .map(SiiOperadorEntity::getSiiPersona)
                .map(SiiPersonaEntity::getPerEmail)
                .orElse("");

        LocalDate fechaFinContrato = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getConFechaFinDefin)
                .orElse(null);

        String tipoVisita = Optional.ofNullable(auto.getAucTipoVisita()).orElse("");

        Collection<FuncionarioDTO> funcionarios = new ArrayList<>();
        if (auto.getSiiGrupoFiscalizacion() != null) {
            funcionarios.addAll(this.mapFuncionarios(auto.getSiiGrupoFiscalizacion()));
        }

        DireccionDTO direccion = this.mapDireccion(auto);

        return ActaDTO.builder()
                .numAuc(auto.getAucNumero())
                .numActa(auto.getAucNumero())
                .fechaVisitaAuc(auto.getAucFechaVisita())
                .numContrato(numContrato)
                .nit(identificacion)
                .estCodigo(estCodigo)
                .estCodigoInterno(estCodigoInterno)
                .conCodigo(conCodigo)
                .funcionarios(funcionarios)
                .nombreOperador(nombreOperador)
                .fechaFinContrato(fechaFinContrato)
                .email(email)
                .tipoVisita(tipoVisita)
                .fechaCorteInventario(LocalDateTime.now())
                .direccion(direccion)
                .build();
    }

    private Collection<FuncionarioDTO> mapFuncionarios(SiiGrupoFiscalizacionEntity fiscalizacion) {
        Collection<FuncionarioDTO> funcionarios = new ArrayList<>();
        if (fiscalizacion.getFsuCodigoPrincip() != null && fiscalizacion.getFsuCodigoPrincip().getSiiPersona() != null) {
            funcionarios.add(this.mapFuncionario(fiscalizacion.getFsuCodigoPrincip().getSiiPersona()));
        }
        if (fiscalizacion.getFsuCodigoAcomp() != null && fiscalizacion.getFsuCodigoAcomp().getSiiPersona() != null) {
            funcionarios.add(this.mapFuncionario(fiscalizacion.getFsuCodigoAcomp().getSiiPersona()));
        }
        return funcionarios;
    }

    private FuncionarioDTO mapFuncionario(SiiPersonaEntity persona) {
        String nombreUsuario = Optional.ofNullable(persona.getSiiUsuarioList())
                .filter(col -> !col.isEmpty())
                .map(col -> col.iterator().next())
                .map(SiiUsuarioEntity::getUsuNombreUsuario)
                .orElse(null);


        return FuncionarioDTO.builder()
                .nombre(persona.getNombreCompleto())
                .identificacion(persona.getPerNumIdentificacion())
                .email(persona.getPerEmail()).email(persona.getPerEmail())
                .cargo(
                        Optional.ofNullable(persona.getSiiUsuarioList())
                                .filter(col -> !col.isEmpty())
                                .map(col -> col.iterator().next())
                                .map(SiiUsuarioEntity::getSiiFuncion1)
                                .map(SiiFuncionEntity::getFunDescripcion)
                                .orElse(null)
                ).idUsuario(nombreUsuario)
                .build();
    }

    private DireccionDTO mapDireccion(SiiAutoComisorioEntity auto) {
        if (auto == null) return new DireccionDTO();

        String tipoVisita = auto.getAucTipoVisita();
        SiiContratoEntity contrato = auto.getSiiContrato();
        SiiEstablecimientoEntity establecimiento = auto.getSiiEstablecimiento();

        if ("F".equalsIgnoreCase(tipoVisita)) {
            return DireccionDTO.builder()
                    .direccion(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getEstDireccion)
                            .orElse(""))
                    .establecimiento(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getEstNombre)
                            .orElse(""))
                    .estCodigo(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getEstCodInterno)
                            .orElse(null))
                    .ciudad(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getSiiUbicacion)
                            .map(SiiUbicacionEntity::getUbiNombre)
                            .orElse(""))
                    .departamento(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getSiiUbicacion)
                            .map(SiiUbicacionEntity::getSiiUbicacionPadre)
                            .map(SiiUbicacionEntity::getUbiNombre)
                            .orElse(""))
                    .latitud(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getEstLatitud)
                            .orElse(null))
                    .longitud(Optional.ofNullable(establecimiento)
                            .map(SiiEstablecimientoEntity::getEstLongitud)
                            .orElse(null))
                    .build();
        }

        SiiPersonaEntity persona = Optional.ofNullable(contrato)
                .map(SiiContratoEntity::getSiiOperadorEntity)
                .map(SiiOperadorEntity::getSiiPersona)
                .orElse(null);

        return DireccionDTO.builder()
                .direccion(Optional.ofNullable(persona)
                        .map(SiiPersonaEntity::getPerDireccion)
                        .orElse(""))
                .ciudad(Optional.ofNullable(persona)
                        .map(SiiPersonaEntity::getSiiUbicacion)
                        .map(SiiUbicacionEntity::getUbiNombre)
                        .orElse(""))
                .departamento(Optional.ofNullable(persona)
                        .map(SiiPersonaEntity::getSiiUbicacion)
                        .map(SiiUbicacionEntity::getSiiUbicacionPadre)
                        .map(SiiUbicacionEntity::getUbiNombre)
                        .orElse(""))
                .latitud(null)
                .longitud(null)
                .build();
    }

}
