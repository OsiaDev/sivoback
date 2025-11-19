package com.coljuegos.sivo.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class UserDTO extends User implements Serializable {

    private final String salt;

    private String idUser;

    private String email;

    private String numIdentificacion;

    private Long perCodigo;

    private String fullName;

    public UserDTO(final String username, final Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        this.salt = "";
    }

    public UserDTO(final String username, final String password, final String salt, String numIdentificacion, final Long perCodigo, String fullName, final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.salt = salt;
        this.numIdentificacion = numIdentificacion;
        this.perCodigo = perCodigo;
        this.fullName = fullName;
    }

}
