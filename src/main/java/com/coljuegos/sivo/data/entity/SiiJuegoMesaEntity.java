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
@Table(name = "SII_JUEGO_MESA")
public class SiiJuegoMesaEntity implements Serializable {

    @Id
    @Column(name = "JME_CODIGO", nullable = false)
    private Long jmeCodigo;

    @Column(name = "JME_NOMBRE", nullable = false, length = 20)
    private String jmeNombre;

}
