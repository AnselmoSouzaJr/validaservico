package com.example.validaservico.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class JwtValidationService {

    private static final Logger logger = LoggerFactory.getLogger(JwtValidationService.class);

    // Lista de roles aceitas
    private static final List<String> VALID_ROLES = Arrays.asList("Admin", "Member", "External");

    /**
     * Método auxiliar para verificar se um número é primo
     */
    private boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    /**
     * Valida o JWT, ignorando a verificação de assinatura.
     *
     * @param jwt Token JWT fornecido.
     * @return true se o JWT é válido, caso contrário false.
     */
    public boolean isValid(String jwt) {
        logger.info("Validando o JWT (permitindo assinaturas não verificadas)...");

        try {
            // Decodificação do JWS (sem validação da assinatura)
            Claims claims = Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(60) // Permite tolerância de 60s no tempo
                    .build() // Sem configurar a chave de assinatura
                    .parseClaimsJws(jwt) // Analisa JWS, mas ignora a validação da assinatura
                    .getBody();

            logger.info("Claims recebidos: {}", claims);

            // Validação da claim "Name"
            String name = claims.get("Name", String.class);
            if (name == null || name.length() > 256 || name.matches(".*\\d.*")) {
                logger.warn("Falha na validação do campo Name: {}", name);
                return false;
            }

            // Validação da claim "Role"
            String role = claims.get("Role", String.class);
            if (role == null || !VALID_ROLES.contains(role)) {
                logger.warn("Falha na validação do campo Role: {}", role);
                return false;
            }

            // Validação da claim "Seed"
            String seedStr = claims.get("Seed", String.class);
            Integer seed = null;
            try {
                seed = Integer.parseInt(seedStr);
            } catch (NumberFormatException e) {
                logger.warn("Falha ao converter Seed: {}, erro: {}", seedStr, e.getMessage());
                return false;
            }
            if (seed == null || !isPrime(seed)) {
                logger.warn("Claim 'Seed' inválida: {}", seed);
                return false;
            }

            logger.info("JWT validado com sucesso!");
            return true;

        } catch (Exception e) {
            logger.error("Erro ao validar o JWT", e);
            return false;
        }
    }
}