package com.example.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/port")
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping
    public String getServerPort() {
        return serverPort;
    }
}
