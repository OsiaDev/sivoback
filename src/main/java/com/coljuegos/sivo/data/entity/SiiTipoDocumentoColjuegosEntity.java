package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_DOCUMENTO_COLJUEGOS")
public class SiiTipoDocumentoColjuegosEntity implements Serializable {

    @Id
    @Column(name = "TDO_CODIGO", nullable = false)
    @SequenceGenerator(name = "SEQ_TIPO_DOC_COLJ_CODIGO", sequenceName = "SEQ_TIPO_DOC_COLJ_CODIGO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TIPO_DOC_COLJ_CODIGO")
    private Long tdoCodigo;

    @Column(name = "TDO_DESCRIPCION", nullable = false, length = 100)
    private String tdoDescripcion;

    @Column(name = "TDO_NOMBRE", nullable = false, length = 50)
    private String tdoNombre;

}
