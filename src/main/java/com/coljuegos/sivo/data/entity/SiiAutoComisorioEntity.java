package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_AUTO_COMISORIO")
public class SiiAutoComisorioEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_AUTO_COMISORIO_COD")
    @SequenceGenerator(name = "SEQ_AUTO_COMISORIO_COD", sequenceName = "SEQ_AUTO_COMISORIO_COD", allocationSize = 1)
    @Column(name = "AUC_CODIGO")
    private Long aucCodigo;

    @Column(name = "AUC_ESTADO")
    private String aucEstado;

    @Column(name = "AUC_FECHA")
    private LocalDate aucFecha;

    @Column(name = "AUC_FECHA_ANULAC")
    private LocalDate aucFechaAnulac;

    @Column(name = "AUC_FECHA_VISITA")
    private LocalDate aucFechaVisita;

    @Column(name = "AUC_MOTIVO_ANULAC")
    private String aucMotivoAnulac;

    @Column(name = "AUC_NUMERO")
    private Integer aucNumero;

    @Column(name = "AUC_TIPO_VISITA")
    private String aucTipoVisita;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUC_ESTADO_VISITA")
    private EstadoVisita estadoVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CON_CODIGO")
    private SiiContratoEntity siiContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EST_CODIGO")
    private SiiEstablecimientoEntity siiEstablecimiento;

    @ManyToOne
    @JoinColumn(name = "GFI_CODIGO")
    private SiiGrupoFiscalizacionEntity siiGrupoFiscalizacion;

}
