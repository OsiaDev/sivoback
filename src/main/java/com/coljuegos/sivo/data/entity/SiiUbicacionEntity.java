package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SII_UBICACION")
public class SiiUbicacionEntity implements Serializable {

    @Id
    @Column(name = "UBI_CODIGO", nullable = false)
    private String ubiCodigo;

    @Column(name = "UBI_CODIGO_PADRE", length = 20, insertable = false, updatable = false)
    private String ubiCodigoPadre;

    @Column(name = "UBI_DESCRIPCION", length = 100)
    private String ubiDescripcion;

    @Column(name = "UBI_NOMBRE", nullable = false, length = 100)
    private String ubiNombre;

    @Column(name = "UBI_LATITUD")
    private BigDecimal ubiLatitud;

    @Column(name = "UBI_LONGITUD")
    private BigDecimal ubiLongitud;

    @ManyToOne
    @JoinColumn(name = "UBI_CODIGO_PADRE")
    private SiiUbicacionEntity siiUbicacionPadre;

    @ManyToOne
    @JoinColumn(name = "TIU_CODIGO")
    private SiiTipoUbicacionEntity siiTipoUbicacion;

}
