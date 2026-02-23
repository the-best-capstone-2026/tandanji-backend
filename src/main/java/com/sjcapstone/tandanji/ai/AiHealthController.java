package com.sjcapstone.tandanji.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiHealthController {

    private final AiClient aiClient;

    @GetMapping("/api/health")
    public HealthResponse health() {
        try {
            aiClient.checkHealth();
            return new HealthResponse("UP", "UP");
        } catch (Exception e) {
            return new HealthResponse("UP", "DOWN");
        }
    }

    public record HealthResponse(String status, String ai) {}
}
