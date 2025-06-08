package vn.edu.hcmuaf.fit.springbootserver.service;

import vn.edu.hcmuaf.fit.springbootserver.dto.auth.AuthResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.RegisterRequest;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(String email);
    void changeEmail(String email, String oldEmail, String newEmail);
    void changePassword(String email, String oldPassword, String newPassword);
    User getProfile(String email);
    User updateProfile(String email, User userDetails);
    User findByEmail(String email);
} 