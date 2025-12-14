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
@Table(name = "SII_VERIFICACION_CONTRACTUAL")
public class SiiVerificacionContractualEntity implements Serializable {

    @Id
    @Column(name = "VCO_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VERIF_CONTRACTUAL_COD")
    @SequenceGenerator(name = "SEQ_VERIF_CONTRACTUAL_COD", sequenceName = "SEQ_VERIF_CONTRACTUAL_COD", allocationSize = 1)
    private Long vcoCodigo;

    @Column(name = "VCO_NUM_ACTA", nullable = false)
    private Integer vcoNumActa;

    @Column(name = "VCO_AVISO_AUTORIZACION", length = 2)
    private String vcoAvisoAutorizacion;

    @Column(name = "VCO_DIRECCION_CORRESPONDE", length = 2)
    private String vcoDireccionCorresponde;

    @Column(name = "VCO_OTRA_DIRECCION", length = 500)
    private String vcoOtraDireccion;

    @Column(name = "VCO_NOMBRE_EST_CORRESPONDE", length = 2)
    private String vcoNombreEstCorresponde;

    @Column(name = "VCO_OTRO_NOMBRE", length = 500)
    private String vcoOtroNombre;

    @Column(name = "VCO_ACTIVIDADES_DIFERENTES", length = 2)
    private String vcoActividadesDiferentes;

    @Column(name = "VCO_TIPO_ACTIVIDAD", length = 500)
    private String vcoTipoActividad;

    @Column(name = "VCO_ESPECIFICACION_OTROS", length = 500)
    private String vcoEspecificacionOtros;

    @Column(name = "VCO_REGISTROS_MANTENIMIENTO", length = 2)
    private String vcoRegistrosMantenimiento;

    @Column(name = "VCO_FECHA_REGISTRO", nullable = false)
    private LocalDateTime vcoFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}