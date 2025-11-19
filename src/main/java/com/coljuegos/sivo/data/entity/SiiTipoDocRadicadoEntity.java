package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_DOC_RADICADO")
public class SiiTipoDocRadicadoEntity implements Serializable {

    @Id
    @Column(name = "TDR_CODIGO")
    @SequenceGenerator(name = "SEQ_TIPO_DOC_RADICADO_CODIGO", sequenceName = "SEQ_TIPO_DOC_RADICADO_CODIGO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TIPO_DOC_RADICADO_CODIGO")
    private Long tdrCodigo;

    @Basic(optional = false)
    @Column(name = "TDR_NOMBRE", length = 100, nullable = false)
    private String tdrNombre;

}
