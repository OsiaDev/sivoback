package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_OPERADOR")
public class SiiOperadorEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "OPE_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_OPERADOR_COD")
    @SequenceGenerator(name = "SEQ_OPERADOR_COD", sequenceName = "SEQ_OPERADOR_COD", allocationSize = 1)
    private Long opeCodigo;

    @Column(name = "OPE_POTENCIAL", length = 1)
    private String opePotencial;

    @Column(name = "OPE_TIPO_POBLAC", length = 1)
    private String opeTipoPoblac;

    @ManyToOne(optional = true)
    @JoinColumn(name = "TOP_CODIGO")
    private SiiTipoOperadorEntity siiTipoOperador;

    @Basic(optional = false)
    @Column(name = "OPE_ESTADO", length = 1, nullable = false)
    private String opeEstado;

    @Column(name = "OPE_FECHA_INI_INHAB")
    private LocalDate opeFechaIniInhab;

    @Column(name = "OPE_FECHA_FIN_INHAB")
    private LocalDate opeFechaFinInhab;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PER_CODIGO", referencedColumnName = "PER_CODIGO")
    private SiiPersonaEntity siiPersona;

    @Column(name = "OPE_CONTRASENIA_AUTOEXCLUIDOS", length = 50)
    private String opeContraseniaAutoexcluidos;

}
