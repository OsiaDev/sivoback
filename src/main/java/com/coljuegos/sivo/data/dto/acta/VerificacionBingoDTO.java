package com.coljuegos.sivo.data.dto.acta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificacionBingoDTO {
    private String cartonesModulos;
    private String sistemaTecnologico;
    private String sistemaInterconectado;
    private String realizaEventosEspeciales;
    private String tipoBalotera;
    private String valorCartonExpuesto;
}
