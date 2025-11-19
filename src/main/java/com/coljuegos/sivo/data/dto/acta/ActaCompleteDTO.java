package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;
import java.util.List;

@Data
public class ActaCompleteDTO {
    private Integer numActa;
    private Double latitud;
    private Double longitud;
    private ActaVisitaDTO actaVisita;
    private VerificacionContractualDTO verificacionContractual;
    private VerificacionSiplaftDTO verificacionSiplaft;
    private List<InventarioRegistradoDTO> inventariosRegistrados;
    private List<NovedadRegistradaDTO> novedadesRegistradas;
    private FirmaActaDTO firmaActa;
    private List<ImagenDTO> imagenes;
}