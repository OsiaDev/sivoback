package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_NOVEDAD")
public class SiiNovedadEntity implements Serializable {

    @Id
    @Column(name = "NOV_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NOVEDAD_COD")
    @SequenceGenerator(name = "SEQ_NOVEDAD_COD", sequenceName = "SEQ_NOVEDAD_COD", allocationSize = 1)
    private Long novCodigo;

    @Column(name = "NOV_FECHA", nullable = false)
    private LocalDate novFecha;

    @ManyToOne
    @JoinColumn(name = "CON_CODIGO")
    private SiiContratoEntity siiContrato;

    //@ManyToOne
    //@JoinColumn(name = "SAU_CODIGO")
    @Column(name = "SAU_CODIGO")
    private Long siiSolicitudAutoriza;

    //@ManyToOne
    //@JoinColumn(name = "OSI_CODIGO")
    @Column(name = "OSI_CODIGO")
    private Long siiOtrosi;

    @ManyToOne
    @JoinColumn(name = "TNO_CODIGO")
    private SiiTipoNovedadEntity siiTipoNovedad;

    @ManyToOne
    @JoinColumn(name = "LAC_CODIGO")
    private SiiLicenciaAcdvEntity siiLicenciaAcdv;

    @Column(name = "TAN_CODIGO")
    private Long tanCodigo;

    @Column(name = "SCO_CODIGO")
    private Long scoCodigo;

}
