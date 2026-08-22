package com.hinchmart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping({"/health", "/health/**", "/api/health", "/api/health/**", "/api/v1/health", "/api/v1/health/**"})
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "HinchMart Backend API");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }
}
