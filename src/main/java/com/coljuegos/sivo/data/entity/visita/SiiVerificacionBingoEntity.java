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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SII_VERIFICACION_BINGO_SIVO")
public class SiiVerificacionBingoEntity implements Serializable {

    @Id
    @Column(name = "VBI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VERIF_BINGO_SIVO_COD")
    @SequenceGenerator(name = "SEQ_VERIF_BINGO_SIVO_COD", sequenceName = "SEQ_VERIF_BINGO_SIVO_COD", allocationSize = 1)
    private Long vbiCodigo;

    @Column(name = "VBI_NUM_ACTA", nullable = false)
    private Integer vbiNumActa;

    @Column(name = "VBI_CARTONES_MODULOS", length = 500)
    private String vbiCartonesModulos;

    @Column(name = "VBI_SISTEMA_TECNOLOGICO", length = 500)
    private String vbiSistemaTecnologico;

    @Column(name = "VBI_SISTEMA_INTERCONECTADO", length = 500)
    private String vbiSistemaInterconectado;

    @Column(name = "VBI_EVENTOS_ESPECIALES", length = 500)
    private String vbiEventosEspeciales;

    @Column(name = "VBI_TIPO_BALOTERA", length = 500)
    private String vbiTipoBalotera;

    @Column(name = "VBI_VALOR_CARTON", length = 500)
    private String vbiValorCarton;

    @Column(name = "VBI_FECHA_REGISTRO", nullable = false)
    private LocalDateTime vbiFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}
