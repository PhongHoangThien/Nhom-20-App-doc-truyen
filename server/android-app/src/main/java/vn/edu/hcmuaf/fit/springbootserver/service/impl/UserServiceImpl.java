package vn.edu.hcmuaf.fit.springbootserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.AuthResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.auth.RegisterRequest;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;
import vn.edu.hcmuaf.fit.springbootserver.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.springbootserver.repository.UserRepository;
import vn.edu.hcmuaf.fit.springbootserver.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Invalid password for user: " + request.getPassword());
            System.out.println("Encoded password: " + passwordEncoder.encode(request.getPassword()));
            throw new IllegalArgumentException("Invalid password");
        }

        return new AuthResponse(user);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole("USER");

        user = userRepository.save(user);
        return new AuthResponse(user);
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // TODO: Implement password reset logic
    }

    @Override
    public void changeEmail(String email, String oldEmail, String newEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getEmail().equals(oldEmail)) {
            throw new IllegalArgumentException("Current email is incorrect");
        }
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("New email already exists");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User updateProfile(String email, User userDetails) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(userDetails.getFullName());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}