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
@Table(name = "SII_TIPO_INSTRUMENTO")
public class SiiTipoInstrumentoEntity implements Serializable {

    @Id
    @Column(name = "TIN_CODIGO", nullable = false)
    private Long tinCodigo;

    @Column(name = "TIN_NOMBRE", nullable = false, length = 100)
    private String tinNombre;

}
