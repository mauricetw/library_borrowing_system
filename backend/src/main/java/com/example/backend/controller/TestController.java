package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String healthCheck() {
        return "Spring Boot 與 Controller 層連線成功！";
    }
}