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
@Table(name = "SII_DETALLE_FINAC_SOL")
public class SiiDetalleFinacSolEntity implements Serializable {

    @Id
    @Column(name = "DFS_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_DETALLE_FINAC_SOL_COD")
    @SequenceGenerator(name = "SEQ_DETALLE_FINAC_SOL_COD", sequenceName = "SEQ_DETALLE_FINAC_SOL_COD", allocationSize = 1)
    private Long dfsCodigo;

    @Column(name = "DFS_OPERAC_INT")
    private Integer dfsOperacInt;

    @Column(name = "DFS_IMPORTACIONES")
    private Integer dfsImportaciones;

    @Column(name = "DFS_TRANSFERENCIAS")
    private Integer dfsTransferencias;

    @Column(name = "DFS_PRESTAMOS_ME")
    private Integer dfsPrestamosMe;

    @Column(name = "DFS_ORDENES_PAGO")
    private Integer dfsOrdenesPago;

    @Column(name = "DFS_EXPORTACIONES")
    private Integer dfsExportaciones;

    @Column(name = "DFS_REMESAS")
    private Integer dfsRemesas;

    @Column(name = "DFS_GIROS")
    private Integer dfsGiros;

    @Column(name = "DFS_CAMBIOS_DIV")
    private Integer dfsCambiosDiv;

    @Column(name = "DFS_INVERSIONES")
    private Integer dfsInversiones;

    @Column(name = "DFS_INGRESOS_MENS")
    private BigDecimal dfsIngresosMens;

    @Column(name = "DFS_INGR_NO_OPER")
    private BigDecimal dfsIngrNoOper;

    @Column(name = "DFS_ACTIVOS_TOT")
    private BigDecimal dfsActivosTot;

    @Column(name = "DFS_PATRIMONIO_TOT")
    private BigDecimal dfsPatrimonioTot;

    @Column(name = "DFS_EGRESOS_MENS")
    private BigDecimal dfsEgresosMens;

    @Column(name = "DFS_PASIVOS_TOT")
    private BigDecimal dfsPasivosTot;

    @Column(name = "DFS_OTROS_INGR")
    private BigDecimal dfsOtrosIngr;

    @Column(name = "DFS_ADQ_COMPRAV")
    private Integer dfsAdqComprav;

    @Column(name = "DFS_ADQ_DONAC")
    private Integer dfsAdqDonac;

    @Column(name = "DFS_ADQ_NO_POSEE_BIEN")
    private Integer dfsAdqNoPoseeBien;

    @Column(name = "DFS_ADQ_OTRO")
    private Integer dfsAdqOtro;

    @Column(name = "DFS_ADQ_OTRO_CUAL", length = 1000)
    private String dfsAdqOtroCual;

    @Column(name = "DFS_ORI_FON_NEGOCIO")
    private Integer dfsOriFonNegocio;

    @Column(name = "DFS_ORI_FON_SOCIOS")
    private Integer dfsOriFonSocios;

    @Column(name = "DFS_ORI_OTRO")
    private Integer dfsOriOtro;

    @Column(name = "DFS_ORI_CUAL", length = 1000)
    private String dfsOriCual;

    @Column(name = "DFS_EGRE_NO_OPE")
    private BigDecimal dfsEgreNoOpe;

    @Column(name = "DFS_FECHA_INI_CORTE")
    private LocalDate dfsFechaIniCorte;

    @Column(name = "DFS_FECHA_FIN_CORTE")
    private LocalDate dfsFechaFinCorte;

    @Column(name = "DFS_COSTOS_GASTOS_ADM")
    private BigDecimal dfsCostosGastosAdm;

    @Column(name = "DFS_CAPITAL_SOCIAL")
    private BigDecimal dfsCapitalSocial;

    @Column(name = "DFS_CAMBIO_DIVISA")
    private Integer dfsCambioDivisa;

    @Column(name = "DFS_NIVEL_ENDEUD")
    private BigDecimal dfsNivelEndeud;

    @Column(name = "DFS_CAP_TRAB_REQ")
    private BigDecimal dfsCapTrabReq;

    @Column(name = "DFS_INDICE_ACT_TOT")
    private BigDecimal dfsIndiceActTot;

    @Column(name = "DFS_PATRIMON_REQ")
    private BigDecimal dfsPatrimonReq;

    @Column(name = "DFS_COST_GAST_ADM_OPE")
    private BigDecimal dfsCostGastAdmOpe;

    @Column(name = "DFS_GAST_INTERESES")
    private BigDecimal dfsGastIntereses;

    @Column(name = "DFS_GAST_FINANCIEROS")
    private BigDecimal dfsGastFinancieros;

    @Column(name = "DFS_UTILIDAD_OPER")
    private BigDecimal dfsUtilidadOper;

    @Column(name = "DFS_UTILIDAD_NETA")
    private BigDecimal dfsUtilidadNeta;

    @Column(name = "DFS_CAPITAL_TRABAJO")
    private BigDecimal dfsCapitalTrabajo;

    @Column(name = "DFS_EBITDA")
    private BigDecimal dfsEbitda;

    @Column(name = "DFS_EBIT")
    private BigDecimal dfsEbit;

    @Column(name = "DFS_RETORNO_ACTIVOS")
    private BigDecimal dfsRetornoActivos;

    @Column(name = "DFS_GAST_FIN_EBITDA")
    private BigDecimal dfsGastFinEbitda;

    @Column(name = "DFS_GAST_FIN_EBIT")
    private BigDecimal dfsGastFinEbit;

    @Column(name = "DFS_RAZON_ENDEUDA")
    private BigDecimal dfsRazonEndeuda;

    @Column(name = "DFS_ACTIVO_CORRIENTE")
    private BigDecimal dfsActivoCorriente;

    @Column(name = "DFS_PASIVO_CORRIENTE")
    private BigDecimal dfsPasivoCorriente;

    @Column(name = "DFS_CAPACIDAD_LIQUIDEZ")
    private BigDecimal dfsCapacidadLiquidez;

    @Column(name = "DFS_LIQUIDEZ")
    private BigDecimal dfsLiquidez;

    @Column(name = "DFS_RAZON_COBERTURA_INT")
    private BigDecimal dfsRazonCoberturaInt;

    @Column(name = "DFS_CUMPLE_CAPITAL_TRABAJ")
    private String dfsCumpleCapitalTrabaj;

    @Column(name = "DFS_CUMPLE_RAZON_COB_INT")
    private String dfsCumpleRazonCobInt;

    @Column(name = "DFS_CUMPLE_NIVEL_ENDEUD")
    private String dfsCumpleNivelEndeud;

    @Column(name = "DFS_CUMPLE_PATRIMONIO_REQ")
    private String dfsCumplePatrimonioReq;

    @Column(name = "DFS_CUMPLE_CAPACIDAD_LIQ")
    private String dfsCumpleCapacidadLiq;

    @Column(name = "DFS_CUMPLE_LIQUIDEZ")
    private String dfsCumpleLiquidez;

    @OneToOne
    @JoinColumn(name = "SAU_CODIGO")
    private SiiSolicitudAutorizaEntity siiSolicitudAutoriza;

    @ManyToOne
    @JoinColumn(name = "MON_CODIGO")
    private SiiMonedaEntity siiMoneda;

}
