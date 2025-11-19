package com.coljuegos.sivo.service.tipoapuesta;

import com.coljuegos.sivo.data.dto.TipoApuestaDTO;
import com.coljuegos.sivo.data.entity.SiiTipoApuestaEntity;
import com.coljuegos.sivo.data.repository.TipoApuestaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class TipoApuestaServiceImpl implements TipoApuestaService {

    private final TipoApuestaRepository tipoApuestaRepository;

    @Override
    public Collection<TipoApuestaDTO> obtenerTodosTiposApuesta() {
        log.info("Obteniendo todos los tipos de apuesta");

        try {
            Collection<SiiTipoApuestaEntity> entities = this.tipoApuestaRepository.findAllOrderByNombre();

            Collection<TipoApuestaDTO> dtos = entities.stream()
                    .map(this::mapToDTO).toList();

            log.info("Se obtuvieron {} tipos de apuesta", dtos.size());
            return dtos;

        } catch (Exception e) {
            log.error("Error al obtener tipos de apuesta: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener tipos de apuesta", e);
        }
    }

    private TipoApuestaDTO mapToDTO(SiiTipoApuestaEntity entity) {
        if (entity == null) {
            return null;
        }

        return TipoApuestaDTO.builder()
                .codigoTipoApuesta(entity.getTapCodigo())
                .nombreTipoApuesta(entity.getTapNombre() != null ? entity.getTapNombre() : "")
                .descripcionTipoApuesta(entity.getTapDerechosExpl() != null ? entity.getTapDerechosExpl() : "")
                .build();
    }

}
