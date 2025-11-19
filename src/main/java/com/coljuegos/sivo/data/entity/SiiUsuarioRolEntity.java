package com.coljuegos.sivo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_USUARIO_ROL")
public class SiiUsuarioRolEntity implements Serializable {

    @Id
    @Column(name = "URO_CODIGO", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_USUARIO_ROL_CODIGO")
    @SequenceGenerator(name = "SEQ_USUARIO_ROL_CODIGO", sequenceName = "SEQ_USUARIO_ROL_CODIGO", allocationSize = 1)
    private Long uroCodigo;

    @ManyToOne
    @JoinColumn(name = "USU_CODIGO")
    private SiiUsuarioEntity siiUsuario;

    @ManyToOne
    @JoinColumn(name = "ROL_CODIGO")
    private SiiRolEntity siiRol;

}
