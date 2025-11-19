package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_INVENTARIO")
public class SiiInventarioEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INVENTARIO_COD")
    @SequenceGenerator(name = "SEQ_INVENTARIO_COD", sequenceName = "SEQ_INVENTARIO_COD", allocationSize = 1)
    @Column(name = "INV_CODIGO")
    private Long invCodigo;

    @Column(name = "INV_ESTADO")
    private String invEstado;

    @Column(name = "INV_FECHA_FIN_LIQ")
    private LocalDate invFechaFinLiq;

    @Column(name = "INV_FECHA_FIN_OFI")
    private LocalDate invFechaFinOfi;

    @Column(name = "INV_FECHA_INI_LIQ")
    private LocalDate invFechaIniLiq;

    @Column(name = "INV_FECHA_INI_OFI")
    private LocalDate invFechaIniOfi;

    @Column(name = "INV_PG")
    private String invPg;

    @Column(name = "INV_PG_ORIGINAL")
    private String invPgOriginal;

    @Column(name = "INV_SILLAS")
    private Integer invSillas;

    @Column(name = "INV_BILLETERO")
    private String invBilletero;

    @ManyToOne
    @JoinColumn(name = "NOV_CODIGO")
    private SiiNovedadEntity siiNovedad;

    @ManyToOne
    @JoinColumn(name = "TAP_CODIGO")
    private SiiTipoApuestaEntity siiTipoApuesta;

    @ManyToOne
    @JoinColumn(name = "INS_CODIGO")
    private SiiInstrumentoEntity siiInstrumento;

    @ManyToOne
    @JoinColumn(name = "EST_CODIGO")
    private SiiEstablecimientoEntity siiEstablecimiento;

}
