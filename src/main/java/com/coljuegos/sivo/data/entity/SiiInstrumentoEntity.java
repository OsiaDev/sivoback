package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_INSTRUMENTO")
public class SiiInstrumentoEntity implements Serializable {

    @Id
    @Column(name = "INS_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INSTRUMENTO_COD")
    @SequenceGenerator(name = "SEQ_INSTRUMENTO_COD", sequenceName = "SEQ_INSTRUMENTO_COD", allocationSize = 1)
    private Long insCodigo;

    @Column(name = "INS_ACTIVO")
    private String insActivo;

    @Column(name = "INS_FECHA_MODIFIC")
    private LocalDate insFechaModific;

    @Column(name = "INS_FECHA_REGISTRO")
    private LocalDate insFechaRegistro;

    @ManyToOne
    @JoinColumn(name = "OPE_CODIGO")
    private SiiOperadorEntity siiOperador;

    @ManyToOne
    @JoinColumn(name = "TIN_CODIGO")
    private SiiTipoInstrumentoEntity siiTipoInstrumento;

    @ManyToOne
    @JoinColumn(name = "MET_CODIGO")
    private SiiMetEntity siiMet;

    @ManyToOne
    @JoinColumn(name = "TAP_CODIGO")
    public SiiTipoApuestaEntity siiTipoApuesta;

    @ManyToOne
    @JoinColumn(name = "TAC_CODIGO")
    private SiiTerminalAcdvEntity siiTerminalAcdv;

    @ManyToOne
    @JoinColumn(name = "MCA_CODIGO")
    private SiiMesaCasinoEntity siiMesaCasino;

    @Column(name = "SIN_CODIGO")
    private Long sinCodigo;

}
