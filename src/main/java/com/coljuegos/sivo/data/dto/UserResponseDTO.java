package com.coljuegos.sivo.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponseDTO implements Serializable {

    private String idUser;

    private String nameUser;

    private String fullNameUser;

    private String emailUser;

    private Long perCodigo;

}
