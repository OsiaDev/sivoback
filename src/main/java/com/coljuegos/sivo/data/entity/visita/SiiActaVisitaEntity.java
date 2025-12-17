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
@Table(name = "SII_ACTA_VISITA_SIVO")
public class SiiActaVisitaEntity implements Serializable {

    @Id
    @Column(name = "AVI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ACTA_VISITA_SIVO_COD")
    @SequenceGenerator(name = "SEQ_ACTA_VISITA_SIVO_COD", sequenceName = "SEQ_ACTA_VISITA_SIVO_COD", allocationSize = 1)
    private Long aviCodigo;

    @Column(name = "AVI_NUM_ACTA", nullable = false)
    private Integer aviNumActa;

    @Column(name = "AVI_NOMBRE_PRESENTE", length = 200)
    private String aviNombrePresente;

    @Column(name = "AVI_IDENTIFICACION_PRESENTE", length = 50)
    private String aviIdentificacionPresente;

    @Column(name = "AVI_MUNICIPIO", length = 100)
    private String aviMunicipio;

    @Column(name = "AVI_CARGO_PRESENTE", length = 100)
    private String aviCargoPresente;

    @Column(name = "AVI_EMAIL_PRESENTE", length = 100)
    private String aviEmailPresente;

    @Column(name = "AVI_CORREOS_CONTACTO", length = 500)
    private String aviCorreosContacto;

    @Column(name = "AVI_FECHA_REGISTRO", nullable = false)
    private LocalDateTime aviFechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

}