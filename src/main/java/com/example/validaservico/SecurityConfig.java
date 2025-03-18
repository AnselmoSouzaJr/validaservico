package com.example.validaservico;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilita CSRF apenas se não necessário
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/jwt/validate").permitAll() // Libera o endpoint JWT
                        .anyRequest().authenticated() // Exige autenticação para outros endpoints
                )
                .formLogin().disable() // Desabilita o formulário de login padrão
                .httpBasic().disable(); // Desabilita o Basic Auth (opcional)

        return http.build();
    }
}
