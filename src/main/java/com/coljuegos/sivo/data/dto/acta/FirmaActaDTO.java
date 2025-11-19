package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;

@Data
public class FirmaActaDTO {
    private String nombreFiscalizadorPrincipal;
    private String ccFiscalizadorPrincipal;
    private String cargoFiscalizadorPrincipal;
    private String firmaFiscalizadorPrincipal; // Base64

    private String nombreFiscalizadorSecundario;
    private String ccFiscalizadorSecundario;
    private String cargoFiscalizadorSecundario;
    private String firmaFiscalizadorSecundario; // Base64

    private String nombreOperador;
    private String ccOperador;
    private String cargoOperador;
    private String firmaOperador; // Base64
}