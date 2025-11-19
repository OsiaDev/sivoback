package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_CLASE_JUEGO")
public class SiiClaseJuegoEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CJU_CODIGO", nullable = false)
    private Long cjuCodigo;

    @Basic(optional = false)
    @Column(name = "CJU_NOMBRE", length = 50, nullable = false)
    private String cjuNombre;

}
