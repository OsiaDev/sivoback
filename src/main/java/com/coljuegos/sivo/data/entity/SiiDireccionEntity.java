package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_DIRECCION")
public class SiiDireccionEntity implements Serializable {

    @Id
    @Column(name = "DIR_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_DIRECCION_COD")
    @SequenceGenerator(name = "SEQ_DIRECCION_COD", sequenceName = "SEQ_DIRECCION_COD", allocationSize = 1)
    private Long dirCodigo;

    @Column(name = "DIR_ACTIVO", nullable = false, length = 1)
    private String dirActivo;

    @Column(name = "DIR_CALLE_PPAL", nullable = false)
    private Integer dirCallePpal;

    @Column(name = "DIR_INFO_ADICIONAL", length = 50)
    private String dirInfoAdicional;

    @Column(name = "DIR_NUMERO1", nullable = false)
    private Integer dirNumero1;

    @Column(name = "DIR_NUMERO2", nullable = false)
    private Integer dirNumero2;

    @Column(name = "DIR_BARRIO", length = 100)
    private String dirBarrio;

    @Column(name = "DIR_LOCALIDAD", length = 100)
    private String dirLocalidad;

    @Column(name = "DIR_LATITUD")
    private String dirLatitud;

    @Column(name = "DIR_LONGITUD")
    private String dirLongitud;

    @ManyToOne
    @JoinColumn(name = "TCD_CODIGO")
    private SiiTipoCalleDireccionEntity siiTipoCalleDireccion;

    @ManyToOne
    @JoinColumn(name = "TSD_CODIGO_PPAL")
    private SiiTipoSectorDirecEntity siiTipoSectorDirecPpal;

    @ManyToOne
    @JoinColumn(name = "TSC_CODIGO1_PPAL")
    private SiiTipoSufijo1CalleEntity siiTipoSufijo1CallePpal1;

    @ManyToOne
    @JoinColumn(name = "TSC_CODIGO2_PPAL")
    private SiiTipoSufijo1CalleEntity siiTipoSufijo1CallePpal2;

    @ManyToOne
    @JoinColumn(name = "USU_CODIGO_CONECT")
    private SiiUsuarioEntity siiUsuarioConec;

    @ManyToOne
    @JoinColumn(name = "TSU_CODIGO")
    private SiiTipoSufijo2CalleEntity siiTipoSufijo2Calle;

    @ManyToOne
    @JoinColumn(name = "TSD_CODIGO_NUM2")
    private SiiTipoSectorDirecEntity siiTipoSectorDirecNum2;

    @ManyToOne
    @JoinColumn(name = "TSC_CODIGO_NUM1")
    private SiiTipoSufijo1CalleEntity siiTipoSufijo1CalleNum1;

    @ManyToOne
    @JoinColumn(name = "TZO_CODIGO")
    private SiiTipoZonaEntity siiTipoZona;

    @ManyToOne
    @JoinColumn(name = "UBI_CODIGO")
    private SiiUbicacionEntity ubiCodigo;

}
