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
@Table(name = "SII_TIPO_SUFIJO2_CALLE")
public class SiiTipoSufijo2CalleEntity implements Serializable {

    @Id
    @Column(name = "TSU_CODIGO", nullable = false)
    private Long tsuCodigo;

    @Column(name = "TSU_NOMBRE", nullable = false, length = 1)
    private String tsuNombre;

}
