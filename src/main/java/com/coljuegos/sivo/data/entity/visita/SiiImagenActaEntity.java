package com.coljuegos.sivo.data.entity.visita;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SII_IMAGEN_ACTA")
public class SiiImagenActaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IMAGEN_ACTA_COD")
    @SequenceGenerator(name = "SEQ_IMAGEN_ACTA_COD", sequenceName = "SEQ_IMAGEN_ACTA_COD", allocationSize = 1)
    @Column(name = "IMA_CODIGO")
    private Long imaCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUC_CODIGO", nullable = false)
    private SiiAutoComisorioEntity siiAutoComisorio;

    @Column(name = "IMA_NUM_ACTA", nullable = false)
    private Integer imaNumActa;

    @Column(name = "IMA_NOMBRE_IMAGEN", nullable = false, length = 255)
    private String imaNombreImagen;

    @Column(name = "IMA_PATH_ARCHIVO", nullable = false, length = 500)
    private String imaPathArchivo;

    @Column(name = "IMA_DESCRIPCION", length = 500)
    private String imaDescripcion;

    @Column(name = "IMA_FRAGMENTO_ORIGEN", length = 100)
    private String imaFragmentoOrigen;

    @Column(name = "IMA_TAMANIO_BYTES")
    private Long imaTamanioBytes;

    @Column(name = "IMA_TIPO_MIME", length = 100)
    private String imaTipoMime;

    @Column(name = "IMA_FECHA_CREACION", nullable = false)
    private LocalDateTime imaFechaCreacion;

    @Column(name = "IMA_FECHA_MODIFICACION")
    private LocalDateTime imaFechaModificacion;

    @PrePersist
    protected void onCreate() {
        imaFechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        imaFechaModificacion = LocalDateTime.now();
    }

}