package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_SOLICITUD_OPER_INTERNET")
public class SiiSolicitudOperInternetEntity implements Serializable {

    @Id
    @Column(name = "SOI_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SOL_OPER_INT_COD")
    @SequenceGenerator(name = "SEQ_SOL_OPER_INT_COD", sequenceName = "SEQ_SOL_OPER_INT_COD", allocationSize = 1)
    private Long soiCodigo;

    @Basic(optional = false)
    @Column(name = "SOI_TIEMPO_CONTRATO", nullable = false)
    private Long soiTiempoContrato;

    @Basic(optional = false)
    @Column(name = "SOI_SITIO_WEB", length = 255, nullable = false)
    private String soiSitioWeb;

    @Basic(optional = false)
    @Column(name = "SOI_NUMERO")
    private Integer soiNumero;

    @OneToOne
    @JoinColumn(name = "SAU_CODIGO")
    private SiiSolicitudAutorizaEntity siiSolicitudAutoriza;

    @ManyToOne
    @JoinColumn(name = "CON_CODIGO")
    private SiiContratoEntity siiContrato;

}
