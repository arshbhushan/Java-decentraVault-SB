package com.decentraV.decentraVault.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("Health-check endpoint accessed.");
        return "OK";
    }
}
