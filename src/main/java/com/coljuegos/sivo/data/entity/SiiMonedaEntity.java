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
@Table(name = "SII_MONEDA")
public class SiiMonedaEntity implements Serializable {

    @Id
    @Column(name = "MON_CODIGO", nullable = false)
    private Long monCodigo;

    @Column(name = "MON_ABREVIATURA", nullable = false, length = 5)
    private String monAbreviatura;

    @Column(name = "MON_NOMBRE", nullable = false, length = 30)
    private String monNombre;

}
