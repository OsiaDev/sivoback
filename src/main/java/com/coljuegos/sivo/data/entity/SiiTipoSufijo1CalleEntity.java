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
@Table(name = "SII_TIPO_SUFIJO1_CALLE")
public class SiiTipoSufijo1CalleEntity implements Serializable {

    @Id
    @Column(name = "TSC_CODIGO", nullable = false)
    private Long tscCodigo;

    @Column(name = "TSC_NOMBRE", nullable = false, length = 1)
    private String tscNombre;

}
