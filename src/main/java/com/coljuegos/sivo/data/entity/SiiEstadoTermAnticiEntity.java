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
@Table(name = "SII_ESTADO_TERM_ANTICIP")
public class SiiEstadoTermAnticiEntity implements Serializable {

    @Id
    @Column(name = "ETA_CODIGO", nullable = false)
    private Long etaCodigo;

    @Column(name = "ETA_NOMBRE", nullable = false, length = 20)
    private String etaNombre;

}
