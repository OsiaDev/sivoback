package com.coljuegos.sivo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO implements Serializable {

    private String direccion;

    private String establecimiento;

    private String estCodigo;

    private String ciudad;

    private String departamento;

    private Double latitud;

    private Double longitud;

}
