package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_FABRICANTES")
public class SiiTipoFabricantesEntity implements Serializable {

    @Id
    @Column(name = "TIF_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TIP_FAB_COD ")
    @SequenceGenerator(name = "SEQ_TIP_FAB_COD ", sequenceName = "SEQ_TIP_FAB_COD ", allocationSize = 1)
    private Long tifCodigo;

    @Basic(optional = false)
    @Column(name = "TIF_DESCRIPCION", length = 100, nullable = false)
    private String tifDescripcion;

    @Column(name = "TIF_FECHA_REGISTRO")
    private LocalDate tifFechaRegistro;

}
