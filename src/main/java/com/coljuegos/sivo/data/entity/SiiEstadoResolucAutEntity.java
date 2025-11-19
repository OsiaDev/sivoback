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
@Table(name = "SII_ESTADO_RESOLUC_AUT")
public class SiiEstadoResolucAutEntity implements Serializable {

    @Id
    @Column(name = "ERA_CODIGO", nullable = false)
    private Long eraCodigo;

    @Column(name = "ERA_NOMBRE", nullable = false, length = 20, insertable = false, updatable = false)
    private String eraNombre;

}
