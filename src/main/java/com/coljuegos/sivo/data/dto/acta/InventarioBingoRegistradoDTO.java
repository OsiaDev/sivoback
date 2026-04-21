package com.coljuegos.sivo.data.dto.acta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioBingoRegistradoDTO {
    private String serial;
    private String marca;
    private String codigoApuesta;
    private String estado;
    private Boolean codigoApuestaDiferente;
    private String codigoApuestaDiferenteValor;
    private Boolean sillasDiferente;
    private Integer sillasValor;
    private Integer sillasOriginal;
    private String observaciones;
}
