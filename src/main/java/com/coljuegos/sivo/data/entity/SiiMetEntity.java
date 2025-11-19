package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_MET")
public class SiiMetEntity implements Serializable {

    @Id
    @Column(name = "MET_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MET_COD")
    @SequenceGenerator(name = "SEQ_MET_COD", sequenceName = "SEQ_MET_COD", allocationSize = 1)
    private Long metCodigo;

    @Column(name = "MET_FECHA_FAB")
    private LocalDate metFechaFab;

    @Column(name = "MET_HOMOLOGADO", length = 1)
    private String metHomologado;

    @Column(name = "MET_MARCA_ANTERIOR", length = 50)
    private String metMarcaAnterior;

    @Column(name = "MET_MODELO", length = 40)
    private String metModelo;

    @Column(name = "MET_NUID", length = 20)
    private String metNuid;

    @Column(name = "MET_ONLINE", nullable = false, length = 1)
    private String metOnline;

    @Column(name = "MET_SERIAL", nullable = false, length = 60)
    private String metSerial;

    @Column(name = "MET_UID", length = 60)
    private String metUid;

    @ManyToOne
    @JoinColumn(name = "MAR_CODIGO")
    private SiiMarcaEntity siiMarca;

    @Column(name = "MET_REPORTE_OPE", length = 1)
    private String metReporteOpe;

    @Column(name = "MET_FASE")
    private Integer metFase;

    @Column(name = "MET_FECHA_MARC_ONLINE")
    private LocalDate metFechaMarcOnline;

    @Column(name = "TME_CODIGO")
    private Long tmeCodigo;

    @Column(name = "MET_FECHA_ETIQUETA")
    private LocalDate metFechaEtiqueta;

    @Column(name = "MET_CPU", length = 60)
    private String metCpu;

    @Column(name = "MET_TIPO_INTERFAZ", length = 1)
    private String metTipoInterfaz;

    @Column(name = "MET_TARJETA_INTERFAZ", length = 100)
    private String metTarjetaInterfaz;

    @Column(name = "MET_JUEGO_MISTERIOSO", length = 1)
    private String metJuegoMisterioso;

    @Column(name = "MET_PROTOCOLO")
    private Short metProtocolo;

    @ManyToOne
    @JoinColumn(name = "CME_CODIGO", referencedColumnName = "CME_CODIGO")
    private SiiClaseMetEntity cmeCodigo;

    @ManyToOne
    @JoinColumn(name = "TEM_CODIGO", referencedColumnName = "TEM_CODIGO")
    private SiiTipoElementoMetEntity temCodigo;

    @ManyToOne
    @JoinColumn(name = "UBI_CODIGO", referencedColumnName = "UBI_CODIGO")
    private SiiUbicacionEntity ubiCodigo;

    @ManyToOne
    @JoinColumn(name = "tipo_met_codigo", referencedColumnName = "tme_codigo")
    private SiiTipoMetEntity tipoMetCodigo;

    @Column(name = "MET_ROLOVER")
    private Long rollover;

    @Column(name = "MET_ESTADO", length = 2)
    private String estado;

    @Column(name = "MET_ONLINE_ORIGIN", length = 1)
    private String onlineOrigin;

    @Column(name = "JME_CODIGO")
    private Long jmeCodigo;

}
