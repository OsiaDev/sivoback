package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_MET")
public class SiiTipoMetEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TME_CODIGO", nullable = false)
    private Long tmeCodigo;

    @Basic(optional = false)
    @Column(name = "TME_NOMBRE", length = 80, nullable = false)
    private String tmeNombre;

}
