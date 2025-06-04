package com.recorder.controller.entity.enuns;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin // Permite todas as origens (apenas para desenvolvimento!)
public class HealthCheckController {

    @GetMapping("/api/health-check")
    public Map<String, String> healthCheck() {
        return Map.of(
                "status", "UP",
                "backend", "Spring Boot",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}
