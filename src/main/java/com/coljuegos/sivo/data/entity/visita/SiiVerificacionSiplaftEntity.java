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
@Table(name = "SII_VERIFICACION_SIPLAFT")
public class SiiVerificacionSiplaftEntity implements Serializable {

    @Id
    @Column(name = "VSI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VERIF_SIPLAFT_COD")
    @SequenceGenerator(name = "SEQ_VERIF_SIPLAFT_COD", sequenceName = "SEQ_VERIF_SIPLAFT_COD", allocationSize = 1)
    private Long vsiCodigo;

    @Column(name = "VSI_NUM_ACTA", nullable = false)
    private Integer vsiNumActa;

    @Column(name = "VSI_FORMATO_IDENTIFICACION", length = 2)
    private String vsiFormatoIdentificacion;

    @Column(name = "VSI_MONTO_IDENTIFICACION", length = 500)
    private String vsiMontoIdentificacion;

    @Column(name = "VSI_FORMATO_REPORTE_INTERNO", length = 2)
    private String vsiFormatoReporteInterno;

    @Column(name = "VSI_SENALES_ALERTA", length = 1000)
    private String vsiSenalesAlerta;

    @Column(name = "VSI_CONOCE_CODIGO_CONDUCTA", length = 2)
    private String vsiConoceCodigoConducta;

    @Column(name = "VSI_FECHA_REGISTRO", nullable = false)
    private LocalDateTime vsiFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}