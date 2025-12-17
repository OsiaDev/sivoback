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

    @Column(name = "INR_SERIAL", nullable = false, length = 60)
    private String inrSerial;

    @Column(name = "INR_MARCA", length = 100)
    private String inrMarca;

    @Column(name = "INR_CODIGO_APUESTA", length = 3)
    private String inrCodigoApuesta;

    @Column(name = "INR_ESTADO", length = 20)
    private String inrEstado;

    @Column(name = "INR_VALOR_CREDITO", length = 50)
    private String inrValorCredito;

    @Column(name = "INR_COIN_IN_MET", length = 50)
    private String inrCoinInMet;

    @Column(name = "INR_COIN_OUT_MET", length = 50)
    private String inrCoinOutMet;

    @Column(name = "INR_JACKPOT_MET", length = 50)
    private String inrJackpotMet;

    @Column(name = "INR_COIN_IN_SCLM", length = 50)
    private String inrCoinInSclm;

    @Column(name = "INR_COIN_OUT_SCLM", length = 50)
    private String inrCoinOutSclm;

    @Column(name = "INR_JACKPOT_SCLM", length = 50)
    private String inrJackpotSclm;

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