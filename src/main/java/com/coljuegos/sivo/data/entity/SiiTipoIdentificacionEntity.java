package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_IDENTIFICACION")
public class SiiTipoIdentificacionEntity implements Serializable {

    @Id
    @Column(name = "TID_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TIPO_IDENTIF_CODIGO")
    @SequenceGenerator(name = "SEQ_TIPO_IDENTIF_CODIGO", sequenceName = "SEQ_TIPO_IDENTIF_CODIGO", allocationSize = 1)
    private Long tidCodigo;

    @Column(name = "TID_ACTIVO", nullable = false, length = 1)
    private String tidActivo;

    @Column(name = "TID_NOMBRE", nullable = false, length = 50)
    private String tidNombre;

    @Column(name = "TID_NOMBRE_CORTO", nullable = false, length = 5)
    private String tidNombreCorto;

}
