package com.coljuegos.sivo.data.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TipoApuestaDTO implements Serializable {

    private Long codigoTipoApuesta;

    private String nombreTipoApuesta;

    private String descripcionTipoApuesta;

}
