package com.coljuegos.sivo.controller;

import com.coljuegos.sivo.service.notificacion.ReenvioCorreoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "notificacion")
public class NotificacionPublicController {

    private final ReenvioCorreoService reenvioCorreoService;

    @PostMapping(path = "reenviar")
    public ResponseEntity<Map<String, String>> reenviarCorreo(@RequestParam(name = "numActa") Integer numActa) {
        log.info("NotificacionPublicController.reenviarCorreo - Peticion para reenviar acta: {}", numActa);

        if (numActa == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El parámetro numActa es requerido"));
        }

        try {
            reenvioCorreoService.reenviarCorreoPost(numActa, true);
            return ResponseEntity.ok(Map.of("mensaje", "Solicitud de reenvío en proceso para el acta " + numActa));
        } catch (Exception e) {
            log.error("Error al procesar reenvío de correo: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

}
