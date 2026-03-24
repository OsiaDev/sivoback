package com.coljuegos.sivo.controller.health;

import com.coljuegos.sivo.service.health.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/visita")
    public ResponseEntity<Map<String, Object>> checkVisitaHealth() {
        return ResponseEntity.ok(healthCheckService.checkHealth());
    }

}
