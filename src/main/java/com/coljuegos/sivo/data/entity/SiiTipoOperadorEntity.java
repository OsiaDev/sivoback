package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_OPERADOR")
public class SiiTipoOperadorEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TOP_CODIGO", nullable = false)
    private Long topCodigo;

    @Column(name = "TOP_DESCRIPCION", length = 100)
    private String topDescripcion;

}
