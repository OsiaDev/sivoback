package com.coljuegos.sivo.data.dto.acta;

import lombok.Data;

@Data
public class VerificacionSiplaftDTO {
    private String cuentaFormatoIdentificacion;
    private String montoIdentificacion;
    private String cuentaFormatoReporteInterno;
    private String senalesAlerta;
    private String conoceCodigoConducta;
}