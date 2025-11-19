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
@Table(name = "SII_TIPO_SECTOR_DIREC")
public class SiiTipoSectorDirecEntity implements Serializable {

    @Id
    @Column(name = "TSD_CODIGO", nullable = false)
    private Long tsdCodigo;

    @Column(name = "TSD_NOMBRE", nullable = false, length = 20)
    private String tsdNombre;

}
