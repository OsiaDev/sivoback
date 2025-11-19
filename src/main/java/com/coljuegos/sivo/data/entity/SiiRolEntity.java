package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_ROL")
public class SiiRolEntity implements Serializable {

    @Id
    @Column(name = "ROL_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ROL_CODIGO")
    @SequenceGenerator(name = "SEQ_ROL_CODIGO", sequenceName = "SEQ_ROL_CODIGO", allocationSize = 1)
    private Long rolCodigo;

    @Column(name = "ROL_ACTIVO", nullable = false, length = 1)
    private String rolActivo;

    @Column(name = "ROL_DESCRIPCION", nullable = false, length = 100)
    private String rolDescripcion;

    @Column(name = "ROL_NOMBRE", nullable = false, length = 50)
    private String rolNombre;

}
