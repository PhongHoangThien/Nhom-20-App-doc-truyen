package vn.edu.hcmuaf.fit.springbootserver.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
}