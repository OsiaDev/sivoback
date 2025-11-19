package com.coljuegos.sivo.controller;

import com.coljuegos.sivo.data.dto.AuthRequestDTO;
import com.coljuegos.sivo.data.dto.AuthResponseDTO;
import com.coljuegos.sivo.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping(path = "login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        log.info("AuthController.login {}", authRequest);
        return ResponseEntity.ok(this.authService.auth(authRequest));
    }

}
