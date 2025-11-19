package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_CUENTAS_CONTABLES")
public class SiiCuentasContablesEntity implements Serializable {

    @Id
    @Column(name = "CCO_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CUENTAS_CONTABLES_COD")
    @SequenceGenerator(name = "SEQ_CUENTAS_CONTABLES_COD", sequenceName = "SEQ_CUENTAS_CONTABLES_COD", allocationSize = 1)
    private Long ccoCodigo;

    @Column(name = "CCO_ACUM_TERC", nullable = false, length = 1)
    private String ccoAcumTerc;

    @Column(name = "CCO_CENTRO_COST", nullable = false, length = 1)
    private String ccoCentroCost;

    @Column(name = "CCO_CONC_INF_EXOG", nullable = false, length = 1)
    private String ccoConcInfExog;

    @Column(name = "CCO_CTA_BALANCE", nullable = false, length = 1)
    private String ccoCtaBalance;

    @Column(name = "CCO_CTA_IMPUESTOS", nullable = false, length = 1)
    private String ccoCtaImpuestos;

    @Column(name = "CCO_CTA_RESULT", nullable = false, length = 1)
    private String ccoCtaResult;

    @Column(name = "CCO_DESCRIPCION", nullable = false, length = 100)
    private String ccoDescripcion;

    @Column(name = "CCO_FTE_FINANC", nullable = false, length = 1)
    private String ccoFteFinanc;

    @Column(name = "CCO_NATURALEZA", nullable = false, length = 1)
    private String ccoNaturaleza;

    @Column(name = "CCO_NIVEL1", nullable = false, length = 1)
    private String ccoNivel1;

    @Column(name = "CCO_NIVEL2", length = 1)
    private String ccoNivel2;

    @Column(name = "CCO_NIVEL3", length = 2)
    private String ccoNivel3;

    @Column(name = "CCO_NIVEL4", length = 2)
    private String ccoNivel4;

    @Column(name = "CCO_NIVEL5", length = 2)
    private String ccoNivel5;

    @Column(name = "CCO_NUM_DOC_CONTA", nullable = false, length = 1)
    private String ccoNumDocConta;

    @Column(name = "CCO_OBLIGA_TERC", nullable = false, length = 1)
    private String ccoObligaTerc;

    @Column(name = "CCO_REFERENCIA_1", nullable = false, length = 1)
    private String ccoReferencia1;

    @Column(name = "CCO_REFERENCIA_2", nullable = false, length = 1)
    private String ccoReferencia2;

    @Column(name = "CCO_TIPO_CUENTA", nullable = false, length = 1)
    private String ccoTipoCuenta;

    @Column(name = "CCO_TIP_DOC_CONTA", nullable = false, length = 1)
    private String ccoTipDocConta;

    @Column(name = "CCO_PERMITE_OBL", nullable = false, length = 1)
    private String ccoPermiteObl;

    @Column(name = "CCO_CTA_ACREEDORA", length = 1)
    private String ccoCtaAcreedora;

    @Column(name = "CCO_SISTEMA_CONTABLE", nullable = false, length = 1)
    private String ccoSistemaContable;

    @Column(name = "CCO_CUENTA_CARTERA", length = 1)
    private String ccoCuentaCartera;

    @Column(name = "CCO_CLASIFICACION_CGN", length = 2)
    private String ccoClasificacionCGN;

    @Column(name = "CCO_OPERACION_RECIPROCA", length = 1)
    private String ccoOperacionReciproca;

    @ManyToOne
    @JoinColumn(name = "ECC_CODIGO")
    private SiiEstadoCuentaContableEntity siiEstadoCuentaContable;

    @ManyToOne
    @JoinColumn(name = "PER_CODIGO_CANC_SAL")
    private SiiPersonaEntity siiPersonaCancSaldo;

}
