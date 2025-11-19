package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_ESTADO_CONTRATO")
public class SiiEstadoContratoEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "ECO_CODIGO", nullable = false)
    private Long ecoCodigo;

    @Basic(optional = false)
    @Column(name = "ECO_NOMBRE", length = 40, nullable = false)
    private String ecoNombre;

    @Basic(optional = false)
    @Column(name = "ECO_EST_EJECUCION", length = 20, nullable = false)
    private String ecoEstEjecucion;

}
