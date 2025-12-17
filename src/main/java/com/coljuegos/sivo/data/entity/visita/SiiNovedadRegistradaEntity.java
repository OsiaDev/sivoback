package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar novedades (irregularidades o METs no autorizados)
 * encontradas durante visitas de fiscalización
 */
@Getter
@Setter
@Entity
@Table(name = "SII_NOVEDAD_REGISTRADA_SIVO")
public class SiiNovedadRegistradaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NOVEDAD_REG_SIVO_COD")
    @SequenceGenerator(name = "SEQ_NOVEDAD_REG_SIVO_COD",
            sequenceName = "SEQ_NOVEDAD_REG_SIVO_COD",
            allocationSize = 1)
    @Column(name = "NOR_CODIGO")
    private Long norCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

    @Column(name = "NOR_NUM_ACTA", nullable = false)
    private Integer norNumActa;

    @Column(name = "NOR_SERIAL", nullable = false, length = 60)
    private String norSerial;

    @Column(name = "NOR_MARCA", length = 100)
    private String norMarca;

    @Column(name = "NOR_CODIGO_APUESTA", length = 3)
    private String norCodigoApuesta;

    @Column(name = "NOR_TIENE_PLACA")
    private Integer norTienePlaca; // Oracle: 1=true, 0=false, null=no especificado

    @Column(name = "NOR_OPERANDO", length = 2)
    private String norOperando;

    @Column(name = "NOR_VALOR_CREDITO", length = 50)
    private String norValorCredito;

    @Column(name = "NOR_COIN_IN_MET", length = 50)
    private String norCoinInMet;

    @Column(name = "NOR_COIN_OUT_MET", length = 50)
    private String norCoinOutMet;

    @Column(name = "NOR_JACKPOT_MET", length = 50)
    private String norJackpotMet;

    @Column(name = "NOR_COIN_IN_SCLM", length = 50)
    private String norCoinInSclm;

    @Column(name = "NOR_COIN_OUT_SCLM", length = 50)
    private String norCoinOutSclm;

    @Column(name = "NOR_JACKPOT_SCLM", length = 50)
    private String norJackpotSclm;

    @Column(name = "NOR_OBSERVACIONES", columnDefinition = "TEXT")
    private String norObservaciones;

    @Column(name = "NOR_FECHA_REGISTRO", nullable = false)
    private LocalDateTime norFechaRegistro;

    @Column(name = "NOR_FECHA_MODIFICACION")
    private LocalDateTime norFechaModificacion;

    @PrePersist
    protected void onCreate() {
        norFechaRegistro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        norFechaModificacion = LocalDateTime.now();
    }

}