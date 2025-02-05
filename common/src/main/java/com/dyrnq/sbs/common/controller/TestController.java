package com.dyrnq.sbs.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ip")
    public ResponseEntity<String> getInfo(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        String protocol = request.getHeader("X-Forwarded-Proto");

        // 创建响应体内容
        String responseBody = "Client IP: " + clientIp + ", Protocol: " + protocol;

        // 创建 HttpHeaders 对象并添加额外的头
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Client-IP", clientIp);
        headers.add("X-Protocol", protocol);
        headers.add("X-Protocol-Hide", protocol);

        // 返回 ResponseEntity 并包含头信息
        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }
}
