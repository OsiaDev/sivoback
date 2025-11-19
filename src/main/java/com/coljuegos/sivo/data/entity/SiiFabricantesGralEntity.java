package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_FABRICANTES_GRAL")
public class SiiFabricantesGralEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "FAB_CODIGO", nullable = false)
    private Long fabCodigo;

    @Column(name = "FAB_CLAVE", length = 10)
    private String fabClave;

    @Column(name = "FAB_ENSAMBLADOR ", length = 1)
    private String fabEnsamblador;

    @Column(name = "FAB_FECHA_REGISTRO")
    private LocalDate fabFechaRegistro;

    @Column(name = "PER_CODIGO")
    private Long perCodigo;

    @Column(name = "FAB_NOMBRE")
    private String fabNombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TIF_CODIGO", referencedColumnName = "TIF_CODIGO")
    private SiiTipoFabricantesEntity siiTipoFabricantes;

}
