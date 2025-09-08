package com.example.customse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;
@Configuration
@Order(1) // Highest priority
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        XorCsrfTokenRequestAttributeHandler xorHandler = new XorCsrfTokenRequestAttributeHandler();
        xorHandler.setCsrfRequestAttributeName("_csrf");
        return http
                .securityMatcher("/web/**")
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/web/auth/signin",
                                "/web/auth/signup",
                                "/web/auth/csrf",
                                "/web/auth/refresh-token"
                        )
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // stores token in a readable cookie
                        .csrfTokenRequestHandler(xorHandler) // uses XOR to reduce exposure to BREACH attacks
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/web/roles/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/web/**").permitAll()
                        .requestMatchers( "/web/auth/signup", "/web/auth/signin","/web/auth/csrf").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                )
                .build();
    }
}
