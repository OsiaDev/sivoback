package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SII_FISCALIZADOR_SUSTANC")
public class SiiFiscalizadorSustancEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FISCALIZADOR_SUSTANC_COD")
    @SequenceGenerator(name = "SEQ_FISCALIZADOR_SUSTANC_COD", sequenceName = "SEQ_FISCALIZADOR_SUSTANC_COD", allocationSize = 1)
    @Column(name = "FSU_CODIGO")
    private Long fsuCodigo;

    @Column(name = "FSU_ROL")
    private String fsuRol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_CODIGO")
    private SiiPersonaEntity siiPersona;

    @Column(name = "TAC_CODIGO")
    private BigDecimal tacCodigo;

}
