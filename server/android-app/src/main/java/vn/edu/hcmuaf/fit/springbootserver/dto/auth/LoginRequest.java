package vn.edu.hcmuaf.fit.springbootserver.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
} 