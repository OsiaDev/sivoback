package com.coljuegos.sivo.controller;

import com.coljuegos.sivo.data.dto.ObtenerActaResponseDTO;
import com.coljuegos.sivo.data.dto.UserDTO;
import com.coljuegos.sivo.data.dto.acta.ActaCompleteDTO;
import com.coljuegos.sivo.data.dto.acta.ActaSincronizacionResponseDTO;
import com.coljuegos.sivo.service.acta.ActaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "acta")
public class ActaController {

    private final ActaService actaService;

    @GetMapping(path = "obtenerActas")
    public ResponseEntity<ObtenerActaResponseDTO> obtenerActas(@AuthenticationPrincipal UserDTO usuario) {
        log.info("ActaController.obtenerActas {}", usuario);
        if (usuario == null) {
            throw new BadCredentialsException("Error en el token");
        }
        ObtenerActaResponseDTO response = this.actaService.obtenerActas(usuario.getPerCodigo());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "upload")
    public ResponseEntity<ActaSincronizacionResponseDTO> upload(@AuthenticationPrincipal UserDTO usuario,
                                                                @Validated @RequestBody ActaCompleteDTO actaCompleteDTO) {
        log.info("ActaController.upload {}", usuario);
        if (usuario == null) {
            throw new BadCredentialsException("Error en el token");
        }
        ObtenerActaResponseDTO response = this.actaService.obtenerActas(usuario.getPerCodigo());
        return ResponseEntity.ok().body(response);
    }

}
