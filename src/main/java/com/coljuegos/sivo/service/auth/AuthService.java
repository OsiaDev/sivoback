package com.coljuegos.sivo.service.auth;

import com.coljuegos.sivo.data.dto.AuthRequestDTO;
import com.coljuegos.sivo.data.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO auth(AuthRequestDTO authRequest);

}
