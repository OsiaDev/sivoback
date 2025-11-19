package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_DOC_COLRADICA")
public class SiiTipoDocColradicaEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "SEQ_TIPO_DOC_COLRADICA_CODIGO", sequenceName = "SEQ_TIPO_DOC_COLRADICA_CODIGO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TIPO_DOC_COLRADICA_CODIGO")
    @Column(name = "TDC_CODIGO")
    private Long tdcCodigo;

    @Basic(optional = false)
    @Column(name = "TDC_OBLIGATORIO", length = 1,nullable = false)
    private String tdcObligatorio;

    @Basic(optional = false)
    @Column(name = "TDC_ACTIVO", length = 1, nullable = false)
    private String tdcActivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TDR_CODIGO", referencedColumnName = "TDR_CODIGO")
    private SiiTipoDocRadicadoEntity siiTipoDocRadicado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TDO_CODIGO", referencedColumnName = "TDO_CODIGO")
    private SiiTipoDocumentoColjuegosEntity siiTipoDocumentoColjuegos;

}
