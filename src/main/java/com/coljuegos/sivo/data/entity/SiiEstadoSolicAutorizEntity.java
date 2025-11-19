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
@Table(name = "SII_ESTADO_SOLIC_AUTORIZ")
public class SiiEstadoSolicAutorizEntity implements Serializable {

    @Id
    @Column(name = "ESA_CODIGO", nullable = false)
    private Long esaCodigo;

    @Column(name = "ESA_NOMBRE", nullable = false, length = 20, insertable = false, updatable = false)
    private String esaNombre;

}
