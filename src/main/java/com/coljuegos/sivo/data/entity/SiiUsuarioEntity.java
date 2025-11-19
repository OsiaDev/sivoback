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
@Table(name = "SII_USUARIO")
public class SiiUsuarioEntity implements Serializable {

    @Id
    @Column(name = "USU_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_USUARIO_CODIGO")
    @SequenceGenerator(name = "SEQ_USUARIO_CODIGO", sequenceName = "SEQ_USUARIO_CODIGO", allocationSize = 1)
    private Long usuCodigo;

    @Column(name = "USU_CONTRASENA", length = 50)
    private String usuContrasena;

    @Column(name = "USU_EMAIL", length = 100)
    private String usuEmail;

    @Column(name = "USU_FECHA_ULTIMO_LOGIN")
    private LocalDate usuFechaUltimoLogin;

    @Column(name = "USU_NOMBRE_USUARIO", nullable = false, length = 20)
    private String usuNombreUsuario;

    @Column(name = "USU_SALT", length = 40)
    private String usuSalt;

    @Column(name = "USU_FECHA_CREACION", nullable = false)
    private LocalDate usuFechaCreacion;

    @Column(name = "USU_USU_SISTEMA", length = 1)
    private String usuUsuSistema;

    @ManyToOne
    @JoinColumn(name = "PER_CODIGO")
    private SiiPersonaEntity siiPersona;

    @ManyToOne
    @JoinColumn(name = "EUS_CODIGO")
    private SiiEstadoUsuarioEntity siiEstadoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FUN_CODIGO")
    private SiiFuncionEntity siiFuncion1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACO_CODIGO")
    private SiiAreaColjuegosEntity siiAreaColjuegos1;

    @OneToMany(mappedBy = "siiUsuario")
    private Collection<SiiUsuarioRolEntity> siiUsuarioRolList;

}
