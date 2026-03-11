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
@Table(name = "SII_VERIFICACION_JUEGO_RESPONSABLE_SIVO")
public class SiiVerificacionJuegoResponsableEntity implements Serializable {

    @Id
    @Column(name = "VJR_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VERIF_JUEGO_RESP_SIVO_COD")
    @SequenceGenerator(name = "SEQ_VERIF_JUEGO_RESP_SIVO_COD", sequenceName = "SEQ_VERIF_JUEGO_RESP_SIVO_COD", allocationSize = 1)
    private Long vjrCodigo;

    @Column(name = "VJR_NUM_ACTA", nullable = false)
    private Integer vjrNumActa;

    @Column(name = "VJR_CUENTA_TEST_IDENT_RIESGOS", length = 2)
    private String vjrCuentaTestIdentRiesgos;

    @Column(name = "VJR_EXISTEN_PIEZAS_PUBLICITARIAS", length = 2)
    private String vjrExistenPiezasPublicitarias;

    @Column(name = "VJR_CUENTA_PROGRAMA_JUEGO_RESP", length = 2)
    private String vjrCuentaProgramaJuegoResp;

    @Column(name = "VJR_FECHA_REGISTRO", nullable = false)
    private LocalDateTime vjrFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}