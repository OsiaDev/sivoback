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
@Table(name = "SII_CONTRATO")
public class SiiContratoEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CON_CODIGO", nullable = false)
    private Long conCodigo;

    @Basic(optional = false)
    @Column(name = "CON_NUMERO", length = 100, nullable = false)
    private String conNumero;

    @Basic(optional = false)
    @Column(name = "CON_FECHA_INI", nullable = false)
    private LocalDate conFechaIni;

    @Basic(optional = false)
    @Column(name = "CON_FECHA_FIN", nullable = false)
    private LocalDate conFechaFin;

    @Basic(optional = false)
    @Column(name = "CON_DESCRIPCION", length = 400, nullable = false)
    private String conDescripcion;

    @Column(name = "CON_PERMISO", length = 10)
    private String conPermiso;

    @Column(name = "CON_FECHA")
    private LocalDate conFecha;

    @Column(name = "CON_CONSECUTIVO")
    private Integer conConsecutivo;

    @Column(name = "CON_VIGENTE", length = 1)
    private String conVigente;

    @Column(name = "BLOCK_CONTRATO")
    private String conBloqEstCuenta;

    @Column(name = "CON_VALOR_ESTIMADO")
    private BigDecimal conValorEstimado;

    @Basic(optional = false)
    @Column(name = "CON_FECHA_REGISTRO", nullable = false)
    private LocalDate conFechaRegistro;

    @Column(name = "CON_EXPEDIENTE_URL", length = 200)
    private String conExpedienteUrl;

    @Column(name = "CON_FECHA_REV_ABOG")
    private LocalDate conFechaRevAbog;

    @Column(name = "CON_TEXTO_VAL_FINAN", length = 1500)
    private String conTextoValFinan;

    @Column(name = "CON_TEXTO_VAL_GCT", length = 1500)
    private String conTextoValGct;

    @Column(name = "CON_FECHA_FIR_COLJ")
    private LocalDate conFechaFirColj;

    @Column(name = "CON_FECHA_CIT_FIR_OPE")
    private LocalDate conFechaCitFirOpe;

    @Column(name = "CON_FECHA_PRG_FIR_OPE")
    private LocalDate conFechaPrgFirOpe;

    @Column(name = "CON_FECHA_FIR_OPE")
    private LocalDate conFechaFirOpe;

    @Column(name = "CON_FECHA_CESION")
    private LocalDate conFechaCesion;

    @Column(name = "CON_TEXTO_VAL_CCA", length = 1500)
    private String conTextoValCca;

    @Column(name = "CON_ES_RENOVACION", length = 1)
    private String conEsRenovacion;

    @Column(name = "CON_FECHA_FIN_DEFIN")
    private LocalDate conFechaFinDefin;

    @Column(name = "CON_FECHA_PASO_A_FIRMA")
    private LocalDate conFechaPasoAFirma;

    @Column(name = "CON_TEXTO_VAL_ABOG", length = 1000)
    private String conTextoValAbog;

    @Column(name = "CON_FECHA_2_ENVIO_ASESOR")
    private LocalDate conFecha2EnvioAsesor;

    @Column(name = "CON_FECHA_ENVIO_ASESOR")
    private LocalDate conFechaEnvioAsesor;

    @Column(name = "CON_FECHA_2_ENVIO_ABOGADO")
    private LocalDate conFecha2EnvioAbogado;

    @Column(name = "CON_FECHA_ENVIO_ABOGADO")
    private LocalDate conFechaEnvioAbogado;

    @Column(name = "AFI_CODIGO_OFICIO_CIT")
    private Long afiCodigoOficiCit;

    @Column(name = "AFI_CODIGO")
    private Long afiCodigo;

    @Column(name = "EFI_CODIGO")
    private Long efiCodigo;

    @Column(name = "CON_RADICADO_OFICIO_CIT")
    private String conRadicadoOficiCit;

    @Column(name = "CON_RADICADO_GES_DOC")
    private String conRadicadoGesDoc;

    @Column(name = "CON_TEXTO_VAL_VICE_OPE", length = 1200)
    private String conTextoValVice;

    @Column(name = "CON_APROB_OFI_CITACION", length = 1)
    private String conAprobOfCitacion;

    @OneToOne(mappedBy = "conCodigoCedente")
    private SiiContratoEntity siiContratoEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CON_CODIGO_CEDENTE", referencedColumnName = "CON_CODIGO")
    private SiiContratoEntity conCodigoCedente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPE_CODIGO")
    private SiiOperadorEntity siiOperadorEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ECO_CODIGO")
    private SiiEstadoContratoEntity siiEstadoContratoEntity;

}
