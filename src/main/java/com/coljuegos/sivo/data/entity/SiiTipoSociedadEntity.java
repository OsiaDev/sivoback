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
@Table(name = "SII_TIPO_SOCIEDAD")
public class SiiTipoSociedadEntity implements Serializable {

    @Id
    @Column(name = "TSO_CODIGO", nullable = false)
    private Long tsoCodigo;

    @Column(name = "TSO_NOMBRE", nullable = false, length = 50)
    private String tsoNombre;

}
