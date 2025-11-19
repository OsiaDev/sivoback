package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_JUEGO")
public class SiiTipoJuegoEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TJU_CODIGO", nullable = false)
    private Long tjuCodigo;

    @Basic(optional = false)
    @Column(name = "TJU_NOMBRE", length = 100, nullable = false)
    private String tjuNombre;

}
