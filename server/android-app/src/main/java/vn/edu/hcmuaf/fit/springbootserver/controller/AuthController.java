package vn.edu.hcmuaf.fit.springbootserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.springbootserver.dto.AuthResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.RegisterRequest;
import vn.edu.hcmuaf.fit.springbootserver.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.getEmail());
        try {
            AuthResponse response = authService.login(loginRequest);
            logger.info("Login successful for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request received for user: {}", registerRequest.getEmail());
        try {
            AuthResponse response = authService.register(registerRequest);
            logger.info("Registration successful for user: {}", registerRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", registerRequest.getEmail(), e);
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        logger.info("Logout request received");
        try {
            authService.logout(token);
            logger.info("Logout successful");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Logout failed", e);
            return ResponseEntity.badRequest().body("Logout failed: " + e.getMessage());
        }
    }
}