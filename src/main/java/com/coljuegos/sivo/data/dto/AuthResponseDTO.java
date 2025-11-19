package com.coljuegos.sivo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthResponseDTO implements Serializable {

    private String token;

    private String tokenType = "Bearer";

    private String username;

    private UserResponseDTO user;

    public AuthResponseDTO(String token, String username, UserResponseDTO user) {
        this.token = token;
        this.username = username;
        this.user = user;
        this.tokenType = "Bearer";
    }

}
