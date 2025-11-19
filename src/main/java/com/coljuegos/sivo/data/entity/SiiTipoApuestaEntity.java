package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPO_APUESTA")
public class SiiTipoApuestaEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "TAP_CODIGO", nullable = false)
    private Long tapCodigo;

    @Basic(optional = false)
    @Column(name = "TAP_NOMBRE", length = 250, nullable = false)
    private String tapNombre;

    @Basic(optional = false)
    @Column(name = "TAP_DERECHOS_EXPL", length = 200, nullable = false)
    private String tapDerechosExpl;

    @Basic(optional = false)
    @Column(name = "TAP_DER_EXPL_FORMULA", length = 50, nullable = false)
    private String tapDerExplFormula;

    @Basic(optional = false)
    @Column(name = "TAP_GASTOS_ADM", length = 50, nullable = false)
    private String tapGastosAdm;

    @Basic(optional = false)
    @Column(name = "TAP_GAST_ADM_FORMULA", length = 50, nullable = false)
    private String tapGastAdmFormula;

    @Basic(optional = false)
    @Column(name = "TAP_CODIGO_APUESTA", length = 3, nullable = false)
    private String tapCodigoApuesta;

    @Basic(optional = false)
    @Column(name = "TAP_APUESTA", length = 20, nullable = false)
    private String tapApuesta;

    @Column(name = "TAP_MIN_SILLAS")
    private Integer tapMinSillas;

    @JoinColumn(name = "CJU_CODIGO", referencedColumnName = "CJU_CODIGO")
    @ManyToOne
    private SiiClaseJuegoEntity siiClaseJuego;

    @JoinColumn(name = "TJU_CODIGO", referencedColumnName = "TJU_CODIGO")
    @ManyToOne(optional = false)
    private SiiTipoJuegoEntity siiTipoJuego;

}
