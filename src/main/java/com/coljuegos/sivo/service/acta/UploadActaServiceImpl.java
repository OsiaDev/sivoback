package com.coljuegos.sivo.service.acta;

import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;
import com.coljuegos.sivo.data.entity.EstadoVisita;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.SiiGrupoFiscalizacionEntity;
import com.coljuegos.sivo.data.repository.AutoComisorioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UploadActaServiceImpl implements UploadActaService {

    private final AutoComisorioRepository autoComisorioRepository;

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

}
