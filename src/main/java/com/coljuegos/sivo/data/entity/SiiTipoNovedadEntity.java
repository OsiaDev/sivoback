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
@Table(name = "SII_TIPO_NOVEDAD")
public class SiiTipoNovedadEntity implements Serializable {

    @Id
    @Column(name = "TNO_CODIGO", nullable = false)
    private Long tnoCodigo;

    @Column(name = "TNO_NOMBRE", nullable = false, length = 50)
    private String tnoNombre;

}
