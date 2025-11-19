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
@Table(name = "SII_ESTADO_OTROSI")
public class SiiEstadoOtrosiEntity implements Serializable {

    @Id
    @Column(name = "EOS_CODIGO", nullable = false)
    private Long eosCodigo;

    @Column(name = "EOS_NOMBRE", nullable = false, length = 30)
    private String eosNombre;

}
