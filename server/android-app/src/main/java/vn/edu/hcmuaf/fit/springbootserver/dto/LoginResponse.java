package vn.edu.hcmuaf.fit.springbootserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private User user;
} 