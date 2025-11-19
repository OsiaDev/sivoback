package com.coljuegos.sivo.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequestDTO implements Serializable {

    private String username;

    private String password;

}
