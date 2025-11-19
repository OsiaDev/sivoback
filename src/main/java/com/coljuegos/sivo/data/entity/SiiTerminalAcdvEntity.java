package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TERMINAL_ACDV")
public class SiiTerminalAcdvEntity implements Serializable {

    @Id
    @Column(name = "TAC_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TERMINAL_ACDV_COD")
    @SequenceGenerator(name = "SEQ_TERMINAL_ACDV_COD", sequenceName = "SEQ_TERMINAL_ACDV_COD", allocationSize = 1)
    private Long tacCodigo;

    @Column(name = "TAC_ANIO_FABRIC", nullable = false)
    private Integer tacAnioFabric;

    @Column(name = "TAC_MODELO", nullable = false, length = 100)
    private String tacModelo;

    @Column(name = "TAC_SERIAL", nullable = false, length = 30)
    private String tacSerial;

    @Column(name = "TAC_NUMERO", nullable = false, length = 30)
    private String tacNumero;

    @ManyToOne
    @JoinColumn(name = "MAR_CODIGO")
    private SiiMarcaEntity siiMarca;

}
