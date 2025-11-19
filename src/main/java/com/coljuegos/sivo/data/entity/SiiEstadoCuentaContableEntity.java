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
@Table(name = "SII_ESTADO_CUENTA_CONTABLE")
public class SiiEstadoCuentaContableEntity implements Serializable {

    @Id
    @Column(name = "ECC_CODIGO", nullable = false)
    private Long eccCodigo;

    @Column(name = "ECC_NOMBRE", nullable = false, length = 20)
    private String eccNombre;

}
