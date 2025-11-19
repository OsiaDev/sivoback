package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_SOLIC_AUTORIZA")
public class SiiTipoSolicAutorizaEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TSA_CODIGO", nullable = false)
    private Long tsaCodigo;

    @Basic(optional = false)
    @Column(name = "TSA_NOMBRE", length = 50, nullable = false)
    private String tsaNombre;

}
