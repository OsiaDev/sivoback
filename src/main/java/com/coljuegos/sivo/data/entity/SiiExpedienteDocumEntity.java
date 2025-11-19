package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_EXPEDIENTE_DOCUM")
public class SiiExpedienteDocumEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "EDO_CODIGO", length = 50, nullable = false)
    private String edoCodigo;

    @Basic(optional = false)
    @Column(name = "EDO_FECHA", nullable = false)
    private LocalDate edoFecha;

    @Column(name = "GDO_CODIGO")
    private Long gdoCodigo;

    @ManyToOne
    @JoinColumn(name = "CON_CODIGO")
    private SiiContratoEntity siiContrato;

    @Column(name = "EDO_NIT")
    private String edoNit;

    @Column(name = "TDM_CODIGO")
    private Long tdmCodigo;

    @ManyToOne
    @JoinColumn(name = "EDO_CODIGO_PADRE", referencedColumnName = "EDO_CODIGO")
    private SiiExpedienteDocumEntity siiExpedienteDocumPadre;

}
