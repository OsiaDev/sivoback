package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_SOLICITUD_AUTORIZA")
public class SiiSolicitudAutorizaEntity implements Serializable {

    @Id
    @Column(name = "SAU_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SOLICITUD_AUTORIZ_COD")
    @SequenceGenerator(name = "SEQ_SOLICITUD_AUTORIZ_COD", sequenceName = "SEQ_SOLICITUD_AUTORIZ_COD",
            allocationSize = 1)
    private Long sauCodigo;

    @Basic(optional = false)
    @Column(name = "SAU_FECHA", nullable = false)
    private LocalDate sauFecha;

    @Column(name = "SAU_NUMERO_SIITO", length = 16)
    private String sauNumeroSiito;

    @Column(name = "SAU_NIT", length = 20)
    private String sauNit;

    @Column(name = "SAU_TIEMPO_CONTR")
    private Short sauTiempoContr;

    @Column(name = "SAU_VALOR_ESTIMADO")
    private BigDecimal sauValorEstimado;

    @Column(name = "SAU_VALOR_PRORROGA")
    private BigDecimal sauValorProrroga;

    @Column(name = "SAU_CODIGO_DESISTIDA")
    private Long sauCodigoDesistida;

    @Column(name = "SAU_MOVIMIENTO_SIITO")
    private Long sauMovimientoSiito;

    @Column(name = "SAU_AMPLIACION")
    private Short sauAmpliacion;

    @Column(name = "SAU_NIT_CESIONARIO", length = 20)
    private String sauNitCesionario;

    @Column(name = "SAU_FLUJO_ACTUAL")
    private Long sauFlujoActual;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ESA_CODIGO", referencedColumnName = "ESA_CODIGO")
    private SiiEstadoSolicAutorizEntity siiEstadoSolicAutoriz;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ESP_CODIGO", referencedColumnName = "ESP_CODIGO")
    private SiiEstadoSolicPortalEntity siiEstadoSolicPortal;

    @ManyToOne
    @JoinColumn(name = "EFI_CODIGO", referencedColumnName = "EFI_CODIGO")
    private SiiExpedienteFisicoEntity siiExpedienteFisico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TSA_CODIGO")
    private SiiTipoSolicAutorizaEntity siiTipoSolicAutoriza;

    @ManyToOne
    @JoinColumn(name = "PER_CODIGO_RIF")
    private SiiPersonaEntity siiPersonaRifaProm;

    @ManyToOne
    @JoinColumn(name = "USU_CODIGO")
    private SiiUsuarioEntity siiUsuario;

    @ManyToOne
    @JoinColumn(name = "EDO_CODIGO")
    private SiiExpedienteDocumEntity siiExpedienteDocum;

    @OneToOne(mappedBy="siiSolicitudAutoriza", optional = false)
    private SiiSolicitudOperInternetEntity siiSolicitudOperInternet;

    @OneToOne(mappedBy="siiSolicitudAutoriza")
    private SiiDetalleFinacSolEntity siiDetalleFinacSol;

    @ManyToOne
    @JoinColumn(name = "EXR_CODIGO_CERTIFICACION", referencedColumnName = "EXR_CODIGO")
    private SiiExpedienteRadicadoEntity siiExpedienteRadicado;

    @Column(name = "SAU_CONCEPTO_CERTIFICACION", length = 30)
    private String sauConceptoCertificacion;

    @Column(name = "SAU_FECHA_FIRMA_VP_OPERA")
    private LocalDate sauFechaFirmaVpOpera;

    @Column(name = "AFI_CODIGO")
    private Integer afiCodigo;

    private transient Long idEstadoSolicAutorizAnterior;

}
