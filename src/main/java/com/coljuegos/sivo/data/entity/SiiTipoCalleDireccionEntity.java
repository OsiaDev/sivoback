package com.coljuegos.sivo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_CALLE_DIRECCION")
public class SiiTipoCalleDireccionEntity implements Serializable {

    @Id
    @Column(name = "TCD_CODIGO", nullable = false)
    private Long tcdCodigo;

    @Column(name = "TCD_NOMBRE", nullable = false, length = 20)
    private String tcdNombre;

    @Column(name = "TDC_ABREVIATURA", nullable = false, length = 2)
    private String tdcAbreviatura;

}
