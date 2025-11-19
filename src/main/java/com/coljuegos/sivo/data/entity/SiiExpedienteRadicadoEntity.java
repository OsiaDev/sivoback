package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Cacheable(false)
@Table(name = "SII_EXPEDIENTE_RADICADO")
public class SiiExpedienteRadicadoEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "EXR_CODIGO", length = 30, nullable = false)
    private String exrCodigo;

    @Basic(optional = false)
    @Column(name = "EXR_FECHA", nullable = false)
    private LocalDate exrFecha;

    @Column(name = "EXR_RADICADO_PORTAL")
    private Integer exrRadicadoPortal;

    @JoinColumn(name = "EDO_CODIGO", referencedColumnName = "EDO_CODIGO")
    @ManyToOne(optional = false)
    private SiiExpedienteDocumEntity siiExpedienteDocum;

}
