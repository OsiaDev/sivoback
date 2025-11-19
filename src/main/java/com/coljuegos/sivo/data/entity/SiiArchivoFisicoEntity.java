package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_ARCHIVO_FISICO")
public class SiiArchivoFisicoEntity implements Serializable {

    @Id
    @Column(name = "AFI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ARCHIVO_FISICO_COD")
    @SequenceGenerator(name = "SEQ_ARCHIVO_FISICO_COD", sequenceName = "SEQ_ARCHIVO_FISICO_COD", allocationSize = 1)
    private Long afiCodigo;

    @Basic(optional = false)
    @Column(name = "AFI_NOMBRE_ORIGNAL", length = 255, nullable = false)
    private String afiNombreOrignal;

    @Basic(optional = false)
    @Column(name = "AFI_NOMBRE_FS", length = 255, nullable = false)
    private String afiNombreFs;

    @Basic(optional = false)
    @Column(name = "AFI_PATH_RELATIVO", length = 255, nullable = false)
    private String afiPathRelativo;

    @Column(name = "AFI_ACTIVO", length = 1)
    private String afiActivo;

    @Column(name = "AFI_FECHA")
    private LocalDate afiFecha;

    @OneToOne(mappedBy = "siiArchivoFisico")
    private SiiArchifiDocColradicaEntity siiArchifiDocColradicaEntity;

}
