package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SII_EXPEDIENTE_FISICO")
public class SiiExpedienteFisicoEntity implements Serializable {

    @Id
    @Column(name = "EFI_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EXPEDIENTE_FISICO_COD")
    @SequenceGenerator(name = "SEQ_EXPEDIENTE_FISICO_COD", sequenceName = "SEQ_EXPEDIENTE_FISICO_COD", allocationSize = 1)
    private Long efiCodigo;

    @Basic(optional = false)
    @Column(name = "EFI_FECHA", nullable = false)
    private LocalDate efiFecha;

    @Basic(optional = false)
    @Column(name = "EFI_ACTIVO", length = 1, nullable = false)
    private String efiActivo;

    @Column(name = "EFI_CODIGO_ORFEO")
    private BigInteger efiCodigoOrfeo;

    @ManyToOne
    @JoinColumn(name = "RAU_CODIGO")
    private SiiResolucionAutorizEntity siiResolucionAutoriz;

}
