package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_MARCA")
public class SiiMarcaEntity implements Serializable {

    @Id
    @Column(name = "MAR_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MARCA_COD")
    @SequenceGenerator(name = "SEQ_MARCA_COD", sequenceName = "SEQ_MARCA_COD", allocationSize = 1)
    private Long marCodigo;

    @Column(name = "MAR_NOMBRE", nullable = false, length = 100)
    private String marNombre;

    @ManyToOne
    @JoinColumn(name = "MAR_CODIGO_NUEVO")
    private SiiMarcaEntity siiMarcaNuevo;

    @Column(name = "MAR_ESTADO", nullable = false, length = 1)
    private String marEstado;

    @ManyToOne
    @JoinColumn(name = "FAB_CODIGO")
    private SiiFabricantesGralEntity siiFabricantesGral;

}
