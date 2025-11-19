package com.coljuegos.sivo.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class ObtenerActaResponseDTO implements Serializable {

    private Collection<ActaDTO>  actas;

}
