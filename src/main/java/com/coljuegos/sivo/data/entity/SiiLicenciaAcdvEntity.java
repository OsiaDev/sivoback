package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_LICENCIA_ACDV")
public class SiiLicenciaAcdvEntity implements Serializable {

    @Id
    @Column(name = "LAC_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LICENCIA_ACDV_COD")
    @SequenceGenerator(name = "SEQ_LICENCIA_ACDV_COD", sequenceName = "SEQ_LICENCIA_ACDV_COD", allocationSize = 1)
    private Long lacCodigo;

    @Column(name = "LAC_MODALIDAD", nullable = false)
    private Integer lacModalidad;

    @Column(name = "LAC_NUMERO", nullable = false, length = 20)
    private String lacNumero;

    @Column(name = "LAC_NUMERO_TERM")
    private Integer lacNumeroTerm;

    @Column(name = "LAC_ESTADO")
    private String lacEstado;

    @JoinColumn(name = "SAU_CODIGO", referencedColumnName = "SAU_CODIGO")
    @OneToOne(optional = false)
    private SiiSolicitudAutorizaEntity siiSolicitudAutoriza;

    @JoinColumn(name = "NOV_CODIGO", referencedColumnName = "NOV_CODIGO")
    @OneToOne(optional = false)
    private SiiNovedadEntity siiNovedad;

}
