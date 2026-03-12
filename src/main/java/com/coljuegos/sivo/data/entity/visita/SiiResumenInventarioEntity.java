package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SII_RESUMEN_INVENTARIO_SIVO")
public class SiiResumenInventarioEntity implements Serializable {

    @Id
    @Column(name = "RSI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESUMEN_INV_SIVO_COD")
    @SequenceGenerator(name = "SEQ_RESUMEN_INV_SIVO_COD", sequenceName = "SEQ_RESUMEN_INV_SIVO_COD", allocationSize = 1)
    private Long rsiCodigo;

    @Column(name = "RSI_NUM_ACTA", nullable = false)
    private Integer rsiNumActa;

    @Column(name = "RSI_NOTAS_RESUMEN", length = 4000)
    private String rsiNotasResumen;

    @Column(name = "RSI_LATITUD", precision = 18)
    private Double rsiLatitud;

    @Column(name = "RSI_LONGITUD", precision = 18)
    private Double rsiLongitud;

    @Column(name = "RSI_FECHA_REGISTRO", nullable = false)
    private LocalDateTime rsiFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}