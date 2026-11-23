package com.autism_rehablito_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/api/health")
    public String checkBackendHealth() {
        return "Autism Centre Backend is running successfully";
    }
}
