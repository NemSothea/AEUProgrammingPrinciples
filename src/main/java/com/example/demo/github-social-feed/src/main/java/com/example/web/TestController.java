package com.example.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Mono<Map<String, String>> test() {
        return Mono.just(Map.of(
            "status", "OK",
            "message", "Application is running",
            "timestamp", java.time.Instant.now().toString()
        ));
    }

    @GetMapping("/")
    public Mono<Map<String, String>> home() {
        return Mono.just(Map.of(
            "message", "GitHub Social Feed API",
            "endpoints", "/api/feed, /api/github/posts, /test"
        ));
    }
}
