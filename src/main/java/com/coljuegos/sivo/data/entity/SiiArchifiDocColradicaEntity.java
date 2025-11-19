package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_ARCHIFI_DOC_COLRADICA")
public class SiiArchifiDocColradicaEntity implements Serializable {

    @Id
    @Column(name = "ATC_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ARCHIFI_DOC_COLRADICA_COD")
    @SequenceGenerator(name = "SEQ_ARCHIFI_DOC_COLRADICA_COD", sequenceName = "SEQ_ARCHIFI_DOC_COLRADICA_COD", allocationSize = 1)
    private Long atcCodigo;

    @JoinColumn(name = "TDC_CODIGO", referencedColumnName = "TDC_CODIGO")
    @ManyToOne(optional = false)
    private SiiTipoDocColradicaEntity siiTipoDocColradica;

    @OneToOne
    @JoinColumn(name = "AFI_CODIGO")
    private SiiArchivoFisicoEntity siiArchivoFisico;

    @ManyToOne
    @JoinColumn(name = "PER_CODIGO")
    private SiiPersonaEntity siiPersona;

}
