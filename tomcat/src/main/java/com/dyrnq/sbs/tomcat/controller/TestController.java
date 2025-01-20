package com.dyrnq.sbs.tomcat.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ip")
    public ResponseEntity<String> getInfo(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        String protocol = request.getHeader("X-Forwarded-Proto");
        return ResponseEntity.ok("Client IP: " + clientIp + ", Protocol: " + protocol);
    }
}
