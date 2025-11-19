package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_FUNCION")
public class SiiFuncionEntity implements Serializable {

    @Id
    @Column(name = "FUN_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FUNCION_COD")
    @SequenceGenerator(name = "SEQ_FUNCION_COD", sequenceName = "SEQ_FUNCION_COD", allocationSize = 1)
    private Long funCodigo;

    @Column(name = "FUN_ACTIVO", nullable = false, length = 1)
    private String funActivo;

    @Column(name = "FUN_DESCRIPCION", nullable = false, length = 100)
    private String funDescripcion;

    @Column(name = "FUN_NOMBRE", nullable = false, length = 80)
    private String funNombre;

}
