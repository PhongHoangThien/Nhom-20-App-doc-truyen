package vn.edu.hcmuaf.fit.springbootserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.AuthResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.RegisterRequest;
import vn.edu.hcmuaf.fit.springbootserver.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login request received: " + loginRequest.getEmail());
        try {
            AuthResponse response = userService.login(loginRequest);
            System.out.println("Login successful for user: " + response.getUser().getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = userService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }
}