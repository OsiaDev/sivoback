package com.coljuegos.sivo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_CONCEPTO_RETEN")
public class SiiConceptoRetenEntity implements Serializable {

    @Id
    @Column(name = "CRE_CODIGO", nullable = false)
    private Long creCodigo;

    @Column(name = "CRE_NOMBRE", nullable = false, length = 60)
    private String creNombre;

}
