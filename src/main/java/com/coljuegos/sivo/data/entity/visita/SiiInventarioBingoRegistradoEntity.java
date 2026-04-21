package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SII_INVENTARIO_BINGO_REG_SIVO")
public class SiiInventarioBingoRegistradoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INV_BINGO_REG_SIVO_COD")
    @SequenceGenerator(name = "SEQ_INV_BINGO_REG_SIVO_COD",
            sequenceName = "SEQ_INV_BINGO_REG_SIVO_COD",
            allocationSize = 1)
    @Column(name = "IBR_CODIGO")
    private Long ibrCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

    @Column(name = "IBR_NUM_ACTA", nullable = false)
    private Integer ibrNumActa;

    @Column(name = "IBR_SERIAL", length = 60)
    private String ibrSerial;

    @Column(name = "IBR_MARCA", length = 100)
    private String ibrMarca;

    @Column(name = "IBR_CODIGO_APUESTA", length = 3)
    private String ibrCodigoApuesta;

    @Column(name = "IBR_ESTADO", length = 20)
    private String ibrEstado;

    @Column(name = "IBR_COD_APUESTA_DIFERENTE")
    private Integer ibrCodApuestaDiferente;

    @Column(name = "IBR_COD_APU_DIFERENTE_VALOR", length = 3)
    private String ibrCodApuestaDiferenteValor;

    @Column(name = "IBR_SILLAS_DIFERENTE")
    private Integer ibrSillasDiferente;

    @Column(name = "IBR_SILLAS_VALOR")
    private Integer ibrSillasValor;

    @Column(name = "IBR_SILLAS_ORIGINAL")
    private Integer ibrSillasOriginal;

    @Column(name = "IBR_OBSERVACIONES", columnDefinition = "TEXT")
    private String ibrObservaciones;

    @Column(name = "IBR_FECHA_REGISTRO", nullable = false)
    private LocalDateTime ibrFechaRegistro;

    @Column(name = "IBR_FECHA_MODIFICACION")
    private LocalDateTime ibrFechaModificacion;

    @PrePersist
    protected void onCreate() {
        ibrFechaRegistro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ibrFechaModificacion = LocalDateTime.now();
    }

}
