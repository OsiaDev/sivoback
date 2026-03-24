package com.coljuegos.sivo.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "acta.reporte")
public class ActaReporteProperties {

    /** Subdirectorio dentro de SIVO_STORAGE_PATH donde se guardan los PDF generados */
    private String jasperSubDir = "jasperReports";

    /** Nombre base del archivo .jrxml en el classpath (sin extensión) */
    private String nombreArchivo = "actaVisitaComercial";

    /** Extensión del archivo de salida */
    private String extension = "pdf";

    private String imagenUno = "LOGO_COLJUEGOS";

    private String imagenDos = "logo_prosperidad_para_todos2";

    private String check = "checked";

    private String noCheck = "unchecked";

}