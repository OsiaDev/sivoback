package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_OTROSI")
public class SiiOtrosiEntity implements Serializable {

    @Id
    @Column(name = "OSI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_OTROSI_COD")
    @SequenceGenerator(name = "SEQ_OTROSI_COD", sequenceName = "SEQ_OTROSI_COD", allocationSize = 1)
    private Long osiCodigo;

    @Column(name = "OSI_CONSECUTIVO")
    private Long osiConsecutivo;

    @Column(name = "OSI_FECHA")
    private LocalDate osiFecha;

    @Column(name = "OSI_FECHA_CIT_OPE")
    private LocalDate osiFechaCitOpe;

    @Column(name = "OSI_FECHA_FIR_COLJ")
    private LocalDate osiFechaFirColj;

    @Column(name = "OSI_FECHA_FIR_OPE")
    private LocalDate osiFechaFirOpe;

    @Column(name = "OSI_FECHA_PROG_FIR")
    private LocalDate osiFechaProgFir;

    @Column(name = "OSI_FECHA_REV_ABOG")
    private LocalDate osiFechaRevAbog;

    @Column(name = "OSI_TEX_VAL_FIN", length = 1500)
    private String osiTexValFin;

    @Column(name = "OSI_TEX_VAL_GCT", length = 1500)
    private String osiTexValGct;

    @ManyToOne
    @JoinColumn(name = "EOS_CODIGO")
    private SiiEstadoOtrosiEntity siiEstadoOtrosi;

    @Column(name = "OSI_TEXTO_VAL_CCA", length = 1500)
    private String osiTextoValCca;

    @ManyToOne
    @JoinColumn(name = "AFI_CODIGO")
    private SiiArchivoFisicoEntity siiArchivoFisico;

    @Column(name = "OSI_FECHA_FIN", nullable = false)
    private LocalDate osiFechaFin;

}
