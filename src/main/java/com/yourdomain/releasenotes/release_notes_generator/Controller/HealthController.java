package com.yourdomain.releasenotes.release_notes_generator.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String healthCheck() {
        return "App is running ✅";
    }
}
