package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "SII_PERSONA")
public class SiiPersonaEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "PER_CODIGO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PERSONA_CODIGO")
    @SequenceGenerator(name = "SEQ_PERSONA_CODIGO", sequenceName = "SEQ_PERSONA_CODIGO", allocationSize = 1)
    private Long perCodigo;

    @Column(name = "PER_PRIMER_NOMBRE", length = 50)
    private String perPrimerNombre;

    @Column(name = "PER_SEGUNDO_NOMBRE", length = 20)
    private String perSegundoNombre;

    @Column(name = "PER_PRIMER_APELLIDO", length = 20)
    private String perPrimerApellido;

    @Column(name = "PER_SEGUNDO_APELLIDO", length = 20)
    private String perSegundoApellido;

    @Basic(optional = false)
    @Column(name = "PER_NUM_IDENTIFICACION", length = 20, nullable = false)
    private String perNumIdentificacion;

    @Basic(optional = false)
    @Column(name = "PER_TIPO_PERSONA", length = 1, nullable = false)
    private String perTipoPersona;

    @Column(name = "PER_JUR_NOMBRE_LARGO", length = 300)
    private String perJurNombreLargo;

    @Column(name = "PER_JUR_NOMBRE_CORTO", length = 300)
    private String perJurNombreCorto;

    @Column(name = "PER_EMAIL", length = 50)
    private String perEmail;

    @Column(name = "PER_TELEFONO", length = 20)
    private String perTelefono;

    @Column(name = "PER_FAX", length = 20)
    private String perFax;

    @Column(name = "PER_DIRECCION", length = 100)
    private String perDireccion;

    @Column(name = "PER_CELULAR", length = 50)
    private String perCelular;

    @Column(name = "PER_ORIGEN", length = 1)
    private String perOrigen;

    @Column(name = "PER_DIGITO_VERIF")
    private Short perDigitoVerif;

    @Column(name = "PER_TELEFONO2", length = 20)
    private String perTelefono2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRE_CODIGO_VENTAS")
    private SiiTipoRetencionEntity siiTipoRetencionVentas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRE_CODIGO_RENTA")
    private SiiTipoRetencionEntity siiTipoRetencionRentas;

    @Column(name = "PER_TIPO_PROVEEDOR", length = 1)
    private String perTipoProveedor;

    @Column(name = "TRE_CODIGO")
    private Long treCodigo;

    @Column(name = "RDI_CODIGO_VENTAS")
    private Long rdiCodigoVentas;

    @Column(name = "RDI_CODIGO_RENTA")
    private Long rdiCodigoRenta;

    @Column(name = "PER_CIUDAD_EXT", length = 200)
    private String perCiudadExt;

    @Column(name = "PER_PAGINA_WEB", length = 100)
    private String perPaginaWeb;

    @Column(name = "PER_PROD_SERV", length = 100)
    private String perProdServ;

    @Column(name = "PER_OBSERV_PROD", length = 1000)
    private String perObservProd;

    @Column(name = "PER_TARJETA_PRO", length = 20)
    private String perTarjetaPro;

    @Column(name = "PER_RIFA_PROMO", length = 1)
    private String perRifaPromo;

    @Column(name = "PER_FECHA_CREA")
    private LocalDate perFechaCrea;

    @Column(name = "PER_ESTAMP_UNAL", length = 1)
    private String perEstampUnal;

    @Column(name = "PER_PLAZO")
    private Short perPlazo;

    @Column(name = "PER_CARGA_LIQ_ACT_ADM", length = 1)
    private String perCargaLiqActAdm;

    @Column(name = "PER_ACTIVO", length = 1)
    private String perActivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USU_CODIGO_CONEC")
    private SiiUsuarioEntity siiUsuarioConec;

    @Column(name = "PER_DIRECCION_ALTERNA", length = 200)
    private String perDireccionAlterna;

    @Column(name = "PER_EMAIL_ALTERNO", length = 60)
    private String perEmailAlterno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TSO_CODIGO")
    private SiiTipoSociedadEntity siiTipoSociedad;

    @JoinColumn(name = "PER_CODIGO_REPRESENTANTE", referencedColumnName = "PER_CODIGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private SiiPersonaEntity perCodigoRepresentante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TID_CODIGO", referencedColumnName = "TID_CODIGO")
    private SiiTipoIdentificacionEntity siiTipoIdentificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBI_CODIGO", referencedColumnName = "UBI_CODIGO")
    private SiiUbicacionEntity siiUbicacion;

    @OneToMany(mappedBy = "siiPersona",  fetch = FetchType.LAZY)
    private Collection<SiiUsuarioEntity> siiUsuarioList;

    public String getNombreCompleto() {
        // Caso: Persona jurídica con NIT
        if (siiTipoIdentificacion != null &&
                31L == (siiTipoIdentificacion.getTidCodigo())) {

            return isNotBlank(perJurNombreLargo) ? perJurNombreLargo : perJurNombreCorto;
        }

        // Caso: Persona natural
        StringBuilder nombre = new StringBuilder();

        if (isNotBlank(perPrimerNombre)) {
            nombre.append(perPrimerNombre.trim());

            if (isNotBlank(perSegundoNombre)) {
                nombre.append(" ").append(perSegundoNombre.trim());
            }
            if (isNotBlank(perPrimerApellido)) {
                nombre.append(" ").append(perPrimerApellido.trim());
            }
            if (isNotBlank(perSegundoApellido)) {
                nombre.append(" ").append(perSegundoApellido.trim());
            }

            return nombre.toString();
        }

        return isNotBlank(perJurNombreLargo) ? perJurNombreLargo : perJurNombreCorto;
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }


}
