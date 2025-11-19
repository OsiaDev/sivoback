package com.coljuegos.sivo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SII_TIPOZONA")
public class SiiTipoZonaEntity implements Serializable {

    @Id
    @Column(name = "TZO_CODIGO", nullable = false)
    private Long tzoCodigo;

    @Column(name = "TZO_NOMBRE", nullable = false, length = 1)
    private String tzoNombre;

    @Column(name = "TZO_ESTADO", nullable = false, length = 1)
    private String tzoEstado;

}
