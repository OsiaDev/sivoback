package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_RETENCION")
public class SiiTipoRetencionEntity implements Serializable {

    @Id
    @Column(name = "TRE_CODIGO", nullable = false, length = 10)
    private String treCodigo;

    @Column(name = "TRE_NOM_CLASIFIC", nullable = false, length = 150)
    private String treNomClasific;

    @ManyToOne
    @JoinColumn(name = "GRE_CODIGO")
    private SiiGrupoRetencEntity siiGrupoRetenc;

    @ManyToOne
    @JoinColumn(name = "CRE_CODIGO")
    private SiiConceptoRetenEntity siiConceptoReten;

    @ManyToOne
    @JoinColumn(name = "CCO_CODIGO")
    private SiiCuentasContablesEntity siiCuentasContables;

    @Column(name = "TRE_OBSERVACIONES", nullable = false, length = 150)
    private String treObservaciones;

    @Column(name = "TRE_RENGLON", nullable = false)
    private Integer treRenglon;

    @Column(name = "TRE_TARIFA", nullable = false)
    private BigDecimal treTarifa;

    @Column(name = "TRE_BASE_UVT", length = 5)
    private String treBaseUvt;

    @Column(name = "TRE_ACTIVO", length = 1, nullable = false)
    private String treActivo;

}
