package com.example.customse.controller.api;

import com.example.customse.entity.Role;
import com.example.customse.entity.User;
import com.example.customse.payload.LoginDto;
import com.example.customse.payload.SignupDto;
import com.example.customse.repository.RoleRepository;
import com.example.customse.repository.UserRepositary;
import com.example.customse.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class ApiAuthController {

    private final UserRepositary userRepositary;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public ApiAuthController(UserRepositary userRepositary, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepositary = userRepositary;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsernameOrEmail(),
                            loginDto.getPassword()
                    )
            );
            MDC.put("requestID", UUID.randomUUID().toString());
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String aceestoken = jwtTokenProvider.generateToken(authentication);
                MDC.put("User", loginDto.getUsernameOrEmail());
                ResponseCookie responseCookie = ResponseCookie.from("aceestoken", aceestoken)
                        .httpOnly(true)
                        .maxAge(Duration.ofMinutes(2))
                        .path("/")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
                log.info("abhiii");
                return ResponseEntity.ok("User authenticated and session created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MDC.clear();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");

    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {

        User user = new User();
        user.setName(signupDto.getName());
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in database"));
        user.setRoles(Collections.singleton(roles));

        userRepositary.save(user);

        return new ResponseEntity<>("User is created", HttpStatus.CREATED);
    }

    @GetMapping("/ad")
    public String getRoles(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return "No authentication info found in session";
        }
        HttpSession session = request.getSession(false);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Session ID: " + (session != null ? session.getId() : "No session") +
                ", Authenticated: " + (auth != null && auth.isAuthenticated() ? auth.getName() : "Anonymous"));
        if (session != null) {
            Enumeration<String> attrs = session.getAttributeNames();
            while (attrs.hasMoreElements()) {
                String attr = attrs.nextElement();
                System.out.println("Session attribute: " + attr + " = " + session.getAttribute(attr));
            }
        }

        // Get roles from authorities
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce("", (acc, role) -> acc + role + " ");
        System.out.println(roles.trim());
        return "User roles: " + roles.trim();
    }
}
