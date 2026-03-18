package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;

@Data
public class NovedadRegistradaDTO {
    private String serial;
    private String marca;
    private String codigoApuesta;
    private Boolean tienePlaca;
    private Boolean descripcionJuego;
    private Boolean planPremios;
    private Boolean valorPremios;
    private String operando;
    private String valorCredito;
    private String coinInMet;
    private String coinOutMet;
    private String jackpotMet;
    private String coinInSclm;
    private String coinOutSclm;
    private String jackpotSclm;
    private String observaciones;
}