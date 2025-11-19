package com.coljuegos.sivo.data.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
public class MaestrosResponseDTO implements Serializable {

    private Collection<TipoApuestaDTO> tiposApuesta;

    private String ultimaActualizacion;

}
