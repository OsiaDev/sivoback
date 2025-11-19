package com.coljuegos.sivo.data.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class ActaDTO implements Serializable {

    private Integer numAuc;

    private LocalDate fechaVisitaAuc;

    private Integer numActa;

    private String numContrato;

    private String nit;

    private Long estCodigo;

    private String estCodigoInterno;

    private Long conCodigo;

    private String nombreOperador;

    private LocalDate fechaFinContrato;

    private String email;

    private String tipoVisita;

    private LocalDateTime fechaCorteInventario;

    private DireccionDTO  direccion;

    private Collection<FuncionarioDTO> funcionarios;

    private Collection<InventarioDTO> inventarios;

}
