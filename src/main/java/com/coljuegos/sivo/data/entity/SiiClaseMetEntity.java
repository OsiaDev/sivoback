package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_CLASE_MET")
public class SiiClaseMetEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CME_CODIGO")
    private Long cmeCodigo;

    @Basic(optional = false)
    @Column(name = "CME_DESCRIPCION", length = 80, nullable = false)
    private String cmeDescripcion;

}
