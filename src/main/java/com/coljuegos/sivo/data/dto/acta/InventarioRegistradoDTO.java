package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;

@Data
public class InventarioRegistradoDTO {
    private String serial;
    private String marca;
    private String codigoApuesta;
    private String estado;
    private String valorCredito;
    private String coinInMet;
    private String coinOutMet;
    private String jackpotMet;
    private String coinInSclm;
    private String coinOutSclm;
    private String jackpotSclm;
    private String observaciones;
    private Boolean codigoApuestaDiferente;
    private String codigoApuestaDiferenteValor;
    private Boolean serialVerificado;
    private String serialDiferente;
    private Boolean descripcionJuego;
    private Boolean planPremios;
    private Boolean valorPremios;
    private Boolean contadoresVerificado;
}