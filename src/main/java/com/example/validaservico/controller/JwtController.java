package com.example.validaservico.controller;

import com.example.validaservico.service.JwtValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtValidationService jwtValidationService;

    public JwtController(JwtValidationService jwtValidationService) {
        this.jwtValidationService = jwtValidationService;
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateJwt(@RequestParam("token") String token) {
        boolean isValid = jwtValidationService.isValid(token);
        if (isValid) {
            return ResponseEntity.ok("JWT válido!");
        }
        return ResponseEntity.ok("JWT inválido.");
    }
}