package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_AREA_COLJUEGOS")
public class SiiAreaColjuegosEntity implements Serializable {

    @Id
    @Column(name = "ACO_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_AREA_COLJUEGOS_COD")
    @SequenceGenerator(name = "SEQ_AREA_COLJUEGOS_COD", sequenceName = "SEQ_AREA_COLJUEGOS_COD", allocationSize = 1)
    private Long acoCodigo;

    @Column(name = "ACO_CODIGO_PADRE")
    private Long acoCodigoPadre;

    @Column(name = "ACO_NOMBRE", nullable = false, length = 50)
    private String acoNombre;

    @Column(name = "ACO_ABREVIATURA", nullable = false, length = 10)
    private String acoAbreviatura;

    @Column(name = "ACO_ACTIVO", nullable = false, length = 20)
    private String acoActivo;

    @Column(name = "ACO_DESCRIPCION", nullable = false, length = 50)
    private String acoDescripcion;

    @Column(name = "ACO_PRESUPUESTO", nullable = false, length = 1)
    private String acoPresupuesto;

}
