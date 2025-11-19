package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_TERMINACION_ANTICIP")
public class SiiTerminacionAnticipEntity implements Serializable {

    @Id
    @Column(name = "TAN_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TERMINACION_ANTICIP_COD")
    @SequenceGenerator(name = "SEQ_TERMINACION_ANTICIP_COD", sequenceName = "SEQ_TERMINACION_ANTICIP_COD", allocationSize = 1)
    private Long tanCodigo;

    @Column(name = "TAN_FECHA_ACTA_TER_ANT")
    private LocalDate tanFechaActaTerAnt;

    @Column(name = "TAN_FECHA_APRO_TER_ANT")
    private LocalDate tanFechaAproTerAnt;

    @Column(name = "TAN_FECHA_RADICA", nullable = false)
    private LocalDate tanFechaRadica;

    @Column(name = "TAN_FECHA_RADIC_RECH")
    private LocalDate tanFechaRadicRech;

    @Column(name = "TAN_FECHA_RAD_DESIST")
    private LocalDate tanFechaRadDesist;

    @Column(name = "TAN_FECHA_TERM_ANTIC")
    private LocalDate tanFechaTermAntic;

    @Column(name = "TAN_FECHA_TERM_SOLIC", nullable = false)
    private LocalDate tanFechaTermSolic;

    @Column(name = "TAN_MOTIVO_DESISTIM", length = 550)
    private String tanMotivoDesistim;

    @Column(name = "TAN_MOTIVO_RECHAZO", length = 250)
    private String tanMotivoRechazo;

    @Column(name = "TAN_MOTIVO_SOLIC", nullable = false, length = 550)
    private String tanMotivoSolic;

    @Column(name = "TAN_RADICADO", nullable = false, length = 30)
    private String tanRadicado;

    @Column(name = "TAN_RADICADO_DESISTIM", length = 30)
    private String tanRadicadoDesistim;

    @Column(name = "TAN_RADICADO_RECHAZO", length = 30)
    private String tanRadicadoRechazo;

    //@ManyToOne
    //@JoinColumn(name = "ETA_CODIGO")
    @Column(name = "ETA_CODIGO")
    private Long siiEstadoTermAnticip;

    @ManyToOne
    @JoinColumn(name = "CON_CODIGO")
    private SiiContratoEntity siiContrato;

}
