package com.coljuegos.sivo.service.maestro;

import com.coljuegos.sivo.data.dto.MaestrosResponseDTO;
import com.coljuegos.sivo.data.dto.TipoApuestaDTO;
import com.coljuegos.sivo.service.tipoapuesta.TipoApuestaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MaestroServiceImpl implements MaestroService {

    private final TipoApuestaService tipoApuestaService;

    @Override
    public MaestrosResponseDTO obtenerMaestros() {
        log.info("Obteniendo maestros");

        try {
            Collection<TipoApuestaDTO> tiposApuesta = this.tipoApuestaService.obtenerTodosTiposApuesta();

            String ultimaActualizacion = LocalDateTime.now()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            MaestrosResponseDTO response = MaestrosResponseDTO.builder()
                    .tiposApuesta(tiposApuesta)
                    .ultimaActualizacion(ultimaActualizacion)
                    .build();

            log.info("Maestros obtenidos exitosamente: {} tipos de apuesta", tiposApuesta.size());
            return response;

        } catch (Exception e) {
            log.error("Error al obtener maestros: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener maestros", e);
        }
    }

}
