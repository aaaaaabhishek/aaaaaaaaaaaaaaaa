package com.example.customse.config;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import com.example.customse.security.AuthProvider.JWTAuthenticationprovider;
import com.example.customse.security.JwtTokenProvider;
import com.example.customse.security.filter.JwtAuthenticationFilter;
import com.example.customse.service.CustomUserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {

        this.userDetailsService = userDetailsService;

        this.jwtTokenProvider = jwtTokenProvider;
    }

    //    @Bean
//    public ServletContextInitializer servletContextInitializer() {
//        return servletContext -> {
//            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
//            sessionCookieConfig.setHttpOnly(true);
//            sessionCookieConfig.setSecure(false);  // Set true if you use HTTPS
//            sessionCookieConfig.setPath("/");
//            // Can't set SameSite directly in standard API, might need to customize response headers or use a filter
//        };
//    }
    @Bean
    public FilterRegistrationBean<MDCInsertingServletFilter> mdcFilter() {
        FilterRegistrationBean<MDCInsertingServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MDCInsertingServletFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // Ensure it runs early
        return registration;
    }

    // JWT AuthenticationProvider for token-based auth
    @Bean
    public JWTAuthenticationprovider jwtAuthenticationProvider() {
        return new JWTAuthenticationprovider(userDetailsService, jwtTokenProvider);
    }

    // AuthenticationManager that knows about both providers
    @Bean
    public AuthenticationManager authenticationManager() {
        // Order matters: if you want username/password attempts to be handled first, keep dao first.
        return new ProviderManager(List.of(daoAuthenticationProvider(), jwtAuthenticationProvider()));
    }

    // JWT filter bean
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager);
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

