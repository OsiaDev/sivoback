package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_RESOLUCION_AUTORIZ")
public class SiiResolucionAutorizEntity implements Serializable {

    @Id
    @Column(name = "RAU_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOLUCION_AUTORIZ")
    @SequenceGenerator(name = "SEQ_RESOLUCION_AUTORIZ", sequenceName = "SEQ_RESOLUCION_AUTORIZ", allocationSize = 1)
    private Long rauCodigo;

    @Column(name = "RAU_FECHA_NOTIF")
    private LocalDate rauFechaNotif;

    @Column(name = "RAU_FECHA_PAS_FIRMA")
    private LocalDate rauFechaPasFirma;

    @Column(name = "RAU_FECHA_PAS_REV")
    private LocalDate rauFechaPasRev;

    @Column(name = "RAU_FECHA_RES_FIRME")
    private LocalDate rauFechaResFirme;

    @Column(name = "RAU_FECHA_RESOL", nullable = false)
    private LocalDate rauFechaResol;

    @Column(name = "RAU_FECHA_SOL_CERTI")
    private LocalDate rauFechaSolCertificación;

    @Column(name = "RAU_FECHA_GENERACION_CERTI")
    private LocalDate rauFechaSolGeneraciónCertificación;

    @Column(name = "CONCEPTO", length = 30)
    private String concepto;

    @Column(name = "RAU_MEDIO_NOTIF", length = 2)
    private String rauMedioNotif;

    @Column(name = "RAU_NUMERO_RES")
    private Long rauNumeroRes;

    @ManyToOne
    @JoinColumn(name = "ERA_CODIGO")
    private SiiEstadoResolucAutEntity siiEstadoResolucAut;

    @ManyToOne
    @JoinColumn(name = "SAU_CODIGO")
    private SiiSolicitudAutorizaEntity siiSolicitudAutoriza;

    @Column(name = "RAU_OBSERVACIONES", length = 500)
    private String rauObservaciones;

    @ManyToOne
    @JoinColumn(name = "AFI_CODIGO")
    private SiiArchivoFisicoEntity siiArchivoFisico;

    @Column(name = "RAU_VALIDAC_FINANC", length = 1100)
    private String rauValidacFinanc;

    @Column(name = "RAU_VALIDAC_GCT", length = 1100)
    private String rauValidacGct;

    @ManyToOne
    @JoinColumn(name = "USU_CODIGO")
    private SiiUsuarioEntity siiUsuario;

    @Column(name = "RAU_FECHA_ASIG_USU")
    private LocalDate rauFechaAsigUsu;

    @ManyToOne
    @JoinColumn(name = "AFI_CODIGO_CONSTANCIA")
    private SiiArchivoFisicoEntity siiArchivoFisicoConstancia;

    @Column(name = "RAU_FECHA_10DIAS_HABILES")
    private LocalDate rauFecha10DiasHabiles;

    @Column(name = "RAU_FECHA_11DIAS_CAMBIO_ESTADO")
    private LocalDate rauFecha11DiasCambioEstado;

    @JoinColumn(name = "USU_CODIGO_CREA")
    private SiiUsuarioEntity siiUsuarioCrea;

}
