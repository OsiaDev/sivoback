package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar las firmas digitales de las actas de visita
 */
@Getter
@Setter
@Entity
@Table(name = "SII_FIRMA_ACTA_SIVO")
public class SiiFirmaActaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FIRMA_ACTA_SIVO_COD")
    @SequenceGenerator(name = "SEQ_FIRMA_ACTA_SIVO_COD", sequenceName = "SEQ_FIRMA_ACTA_SIVO_COD", allocationSize = 1)
    @Column(name = "FIA_CODIGO")
    private Long fiaCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

    @Column(name = "FIA_NUM_ACTA", nullable = false)
    private Integer fiaNumActa;

    // Fiscalizador Principal
    @Column(name = "FIA_NOMBRE_FISC_PRINCIPAL", length = 200)
    private String fiaNombreFiscPrincipal;

    @Column(name = "FIA_CC_FISC_PRINCIPAL", length = 50)
    private String fiaCcFiscPrincipal;

    @Column(name = "FIA_CARGO_FISC_PRINCIPAL", length = 100)
    private String fiaCargoFiscPrincipal;

    @Column(name = "FIA_PATH_FIRMA_FISC_PRINCIPAL", length = 500)
    private String fiaPathFirmaFiscPrincipal;

    // Fiscalizador Secundario
    @Column(name = "FIA_NOMBRE_FISC_SECUNDARIO", length = 200)
    private String fiaNombreFiscSecundario;

    @Column(name = "FIA_CC_FISC_SECUNDARIO", length = 50)
    private String fiaCcFiscSecundario;

    @Column(name = "FIA_CARGO_FISC_SECUNDARIO", length = 100)
    private String fiaCargoFiscSecundario;

    @Column(name = "FIA_PATH_FIRMA_FISC_SECUNDARIO", length = 500)
    private String fiaPathFirmaFiscSecundario;

    // Operador
    @Column(name = "FIA_NOMBRE_OPERADOR", length = 200)
    private String fiaNombreOperador;

    @Column(name = "FIA_CC_OPERADOR", length = 50)
    private String fiaCcOperador;

    @Column(name = "FIA_CARGO_OPERADOR", length = 100)
    private String fiaCargoOperador;

    @Column(name = "FIA_PATH_FIRMA_OPERADOR", length = 500)
    private String fiaPathFirmaOperador;

    // Auditoría
    @Column(name = "FIA_FECHA_REGISTRO", nullable = false)
    private LocalDateTime fiaFechaRegistro;

    @Column(name = "FIA_FECHA_MODIFICACION")
    private LocalDateTime fiaFechaModificacion;

    @PrePersist
    protected void onCreate() {
        fiaFechaRegistro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fiaFechaModificacion = LocalDateTime.now();
    }

}