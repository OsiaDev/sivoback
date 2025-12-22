package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar inventarios de METs registrados durante visitas de fiscalización
 */
@Getter
@Setter
@Entity
@Table(name = "SII_INVENTARIO_REGISTRADO_SIVO")
public class SiiInventarioRegistradoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INVENTARIO_REG_SIVO_COD")
    @SequenceGenerator(name = "SEQ_INVENTARIO_REG_SIVO_COD",
            sequenceName = "SEQ_INVENTARIO_REG_SIVO_COD",
            allocationSize = 1)
    @Column(name = "INR_CODIGO")
    private Long inrCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

    @Column(name = "INR_NUM_ACTA", nullable = false)
    private Integer inrNumActa;

    // ========================================
    // CAMPOS BÁSICOS DEL MET
    // ========================================

    @Column(name = "INR_SERIAL", nullable = false, length = 60)
    private String inrSerial;

    @Column(name = "INR_MARCA", length = 100)
    private String inrMarca;

    @Column(name = "INR_CODIGO_APUESTA", length = 3)
    private String inrCodigoApuesta;

    @Column(name = "INR_ESTADO", length = 20)
    private String inrEstado;

    // ========================================
    // NUEVOS CAMPOS DE VERIFICACIÓN DE CÓDIGO DE APUESTA
    // ========================================

    /**
     * Indicador si el código de apuesta encontrado es diferente al autorizado
     * 1 = Sí es diferente, 0 = No es diferente, NULL = No verificado
     */
    @Column(name = "INR_COD_APUESTA_DIFERENTE")
    private Integer inrCodApuestaDiferente;

    /**
     * Valor del código de apuesta diferente encontrado durante la inspección
     * Requerido si inrCodApuestaDiferente = 1
     */
    @Column(name = "INR_COD_APUESTA_DIFERENTE_VALOR", length = 3)
    private String inrCodApuestaDiferenteValor;

    // ========================================
    // NUEVOS CAMPOS DE VERIFICACIÓN DE SERIAL
    // ========================================

    /**
     * Indicador si el serial fue verificado físicamente durante la inspección
     * 1 = Verificado, 0 = No verificado, NULL = No aplica
     */
    @Column(name = "INR_SERIAL_VERIFICADO")
    private Integer inrSerialVerificado;

    /**
     * Serial diferente al autorizado, encontrado durante la verificación
     * Requerido si inrSerialVerificado = 1 y hay discrepancia
     */
    @Column(name = "INR_SERIAL_DIFERENTE", length = 60)
    private String inrSerialDiferente;

    // ========================================
    // NUEVOS CAMPOS DE VERIFICACIÓN DE CARACTERÍSTICAS
    // ========================================

    /**
     * Verificación de descripción del juego
     * 1 = Conforme, 0 = No conforme, NULL = No verificado
     */
    @Column(name = "INR_DESCRIPCION_JUEGO")
    private Integer inrDescripcionJuego;

    /**
     * Verificación del plan de premios
     * 1 = Conforme, 0 = No conforme, NULL = No verificado
     */
    @Column(name = "INR_PLAN_PREMIOS")
    private Integer inrPlanPremios;

    /**
     * Verificación del valor de premios
     * 1 = Conforme, 0 = No conforme, NULL = No verificado
     */
    @Column(name = "INR_VALOR_PREMIOS")
    private Integer inrValorPremios;

    @Column(name = "INR_VALOR_CREDITO", length = 50)
    private String inrValorCredito;

    // ========================================
    // NUEVO CAMPO DE VERIFICACIÓN DE CONTADORES
    // ========================================

    /**
     * Indicador si los contadores fueron verificados durante la inspección
     * 1 = Verificados, 0 = No verificados, NULL = No aplica
     */
    @Column(name = "INR_CONTADORES_VERIFICADO")
    private Integer inrContadoresVerificado;

    // ========================================
    // CONTADORES DEL MET
    // ========================================

    @Column(name = "INR_COIN_IN_MET", length = 50)
    private String inrCoinInMet;

    @Column(name = "INR_COIN_OUT_MET", length = 50)
    private String inrCoinOutMet;

    @Column(name = "INR_JACKPOT_MET", length = 50)
    private String inrJackpotMet;

    // ========================================
    // CONTADORES DEL SCLM (SISTEMA CENTRAL)
    // ========================================

    @Column(name = "INR_COIN_IN_SCLM", length = 50)
    private String inrCoinInSclm;

    @Column(name = "INR_COIN_OUT_SCLM", length = 50)
    private String inrCoinOutSclm;

    @Column(name = "INR_JACKPOT_SCLM", length = 50)
    private String inrJackpotSclm;

    // ========================================
    // OBSERVACIONES Y AUDITORÍA
    // ========================================

    @Column(name = "INR_OBSERVACIONES", columnDefinition = "TEXT")
    private String inrObservaciones;

    @Column(name = "INR_FECHA_REGISTRO", nullable = false)
    private LocalDateTime inrFechaRegistro;

    @Column(name = "INR_FECHA_MODIFICACION")
    private LocalDateTime inrFechaModificacion;

    @PrePersist
    protected void onCreate() {
        inrFechaRegistro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        inrFechaModificacion = LocalDateTime.now();
    }

}