package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_GRUPO_FISCALIZACION")
public class SiiGrupoFiscalizacionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GRUPO_FISCALIZAC_COD")
    @SequenceGenerator(name = "SEQ_GRUPO_FISCALIZAC_COD", sequenceName = "SEQ_GRUPO_FISCALIZAC_COD", allocationSize = 1)
    @Column(name = "GFI_CODIGO")
    private Long gfiCodigo;

    @Column(name = "GFI_FECHA_FIN")
    private LocalDate gfiFechaFin;

    @Column(name = "GFI_FECHA_INI")
    private LocalDate gfiFechaIni;

    @Column(name = "GFI_NUMERO")
    private String gfiNumero;

    @ManyToOne
    @JoinColumn(name = "FSU_CODIGO_ACOMP")
    private SiiFiscalizadorSustancEntity fsuCodigoAcomp;

    @ManyToOne
    @JoinColumn(name = "FSU_CODIGO_PRINCIP")
    private SiiFiscalizadorSustancEntity fsuCodigoPrincip;

    @Column(name = "ESTADO")
    private String estado;

}
