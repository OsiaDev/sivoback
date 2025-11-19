package com.coljuegos.sivo.data.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FuncionarioDTO implements Serializable {

    private String idUsuario;

    private String nombre;

    private String cargo;

    private String email;

    private String identificacion;

}
