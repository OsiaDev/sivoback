package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_MESA_CASINO")
public class SiiMesaCasinoEntity implements Serializable {

    @Id
    @Column(name = "MCA_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_MESA_CASINO_COD")
    @SequenceGenerator(name = "SEQ_MESA_CASINO_COD", sequenceName = "SEQ_MESA_CASINO_COD", allocationSize = 1)
    private Long mcaCodigo;

    @ManyToOne
    @JoinColumn(name = "JME_CODIGO")
    private SiiJuegoMesaEntity siiJuegoMesa;

}
