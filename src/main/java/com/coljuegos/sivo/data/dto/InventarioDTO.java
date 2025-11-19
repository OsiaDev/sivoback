package com.coljuegos.sivo.data.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class InventarioDTO implements Serializable {

    private String nombreMarca;

    private String metSerial;

    private String insCodigo;

    private Integer invSillas;

    private String tipoApuestaNombre;

    private Boolean metOnline;

    private String codigoTipoApuesta;

    private String nuc;

    private Long conCodigo;

    private Integer aucNumero;

    private Long estCodigo;

}
