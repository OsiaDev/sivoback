package com.coljuegos.sivo.controller;

import com.coljuegos.sivo.data.dto.MaestrosResponseDTO;
import com.coljuegos.sivo.service.maestro.MaestroService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "maestros")
public class MaestroController {

    private final MaestroService maestroService;

    @GetMapping(value = "obtenerMaestros")
    public ResponseEntity<MaestrosResponseDTO> obtenerMaestros() {
        log.info("MaestroController.obtenerMaestros");
        MaestrosResponseDTO response = this.maestroService.obtenerMaestros();
        return ResponseEntity.ok(response);
    }

}
