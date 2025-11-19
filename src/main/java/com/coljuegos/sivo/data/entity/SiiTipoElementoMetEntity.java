package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_ELEMENTO_MET")
public class SiiTipoElementoMetEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TEM_CODIGO", nullable = false)
    private Long temCodigo;

    @Basic(optional = false)
    @Column(name = "TEM_DESCRIPCION", length = 80, nullable = false)
    private String temDescripcion;

}
