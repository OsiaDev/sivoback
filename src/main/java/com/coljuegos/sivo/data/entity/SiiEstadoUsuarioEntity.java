package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_ESTADO_USUARIO")
public class SiiEstadoUsuarioEntity implements Serializable {

    @Id
    @Column(name = "EUS_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ESTADO_USUARIO_CODIGO")
    @SequenceGenerator(name = "SEQ_ESTADO_USUARIO_CODIGO", sequenceName = "SEQ_ESTADO_USUARIO_CODIGO", allocationSize = 1)
    private Long eusCodigo;

    @Column(name = "EUS_DESCRIPCION", nullable = false, length = 100, insertable = false, updatable = false)
    private String eusDescripcion;

    @Column(name = "EUS_NOMBRE", nullable = false, length = 20, insertable = false, updatable = false)
    private String eusNombre;

}
