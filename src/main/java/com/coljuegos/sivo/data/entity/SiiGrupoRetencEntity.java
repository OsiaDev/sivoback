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
@Table(name = "SII_GRUPO_RETENC")
public class SiiGrupoRetencEntity implements Serializable {

    @Id
    @Column(name = "GRE_CODIGO", nullable = false)
    private Long greCodigo;

    @Column(name = "GRE_NOMBRE", nullable = false, length = 50)
    private String greNombre;

}
