package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_ESTABLECIMIENTO")
public class SiiEstablecimientoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ESTABLECIMIENTO_COD")
    @SequenceGenerator(name = "SEQ_ESTABLECIMIENTO_COD", sequenceName = "SEQ_ESTABLECIMIENTO_COD", allocationSize = 1)
    @Column(name = "EST_CODIGO")
    private Long estCodigo;

    @Column(name = "EST_COD_INTERNO")
    private String estCodInterno;

    @Column(name = "EST_DIRECCION")
    private String estDireccion;

    @Column(name = "EST_ESTADO")
    private String estEstado;

    @Column(name = "EST_HORA_CORTE_OPERACION")
    private LocalDate estHoraCorteOperacion;

    @Column(name = "EST_LATITUD")
    private Double estLatitud;

    @Column(name = "EST_LONGITUD")
    private Double estLongitud;

    @Column(name = "EST_NOMBRE")
    private String estNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOV_CODIGO")
    private SiiNovedadEntity siiNovedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPE_CODIGO")
    private SiiOperadorEntity siiOperador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBI_CODIGO")
    private SiiUbicacionEntity siiUbicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIR_CODIGO")
    private SiiDireccionEntity siiDireccion;

    @Column(name = "EST_AREA")
    private Integer estArea;

    @Column(name = "EST_CIIU_PRINCIPAL")
    private String estCiiuPrincipal;

    @Column(name = "EST_CIIU_SECUNDARIO")
    private String estCiiuSecundario;

}
