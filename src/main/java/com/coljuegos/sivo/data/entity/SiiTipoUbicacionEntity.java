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
@Table(name = "SII_TIPO_UBICACION")
public class SiiTipoUbicacionEntity implements Serializable {

    @Id
    @Column(name = "TIU_CODIGO", nullable = false)
    private Long tiuCodigo;

    @Column(name = "TIU_NOMBRE", nullable = false, length = 30)
    private String tiuNombre;

}
