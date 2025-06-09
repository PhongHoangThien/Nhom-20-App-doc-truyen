package vn.edu.hcmuaf.fit.springbootserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;
import vn.edu.hcmuaf.fit.springbootserver.entity.UserToken;
import vn.edu.hcmuaf.fit.springbootserver.repository.UserRepository;
import vn.edu.hcmuaf.fit.springbootserver.repository.UserTokenRepository;
import vn.edu.hcmuaf.fit.springbootserver.security.JwtTokenProvider;
import vn.edu.hcmuaf.fit.springbootserver.dto.AuthResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.RegisterRequest;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Lấy user từ database thay vì từ authentication
            User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            logger.info("User authenticated successfully: {}", user.getEmail());
            
            // Tạo token mới
            String token = jwtTokenProvider.generateToken(authentication);
            logger.info("Generated JWT token for user: {}", user.getEmail());
            
            // Lưu token vào database
            UserToken userToken = new UserToken();
            userToken.setUser(user);
            userToken.setToken(token);
            userTokenRepository.save(userToken);
            logger.info("Token saved to database for user: {}", user.getEmail());

            return new AuthResponse(token, user);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", passwordEncoder.encode(loginRequest.getPassword()), e);
            logger.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            throw e;
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        logger.info("Attempting to register new user: {}", registerRequest.getEmail());
        try {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                logger.warn("Email already exists: {}", registerRequest.getEmail());
                throw new RuntimeException("Email already exists");
            }

            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole("USER");
            userRepository.save(user);
            logger.info("New user registered: {}", user.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Tạo token mới
            String token = jwtTokenProvider.generateToken(authentication);
            logger.info("Generated JWT token for new user: {}", user.getEmail());
            
            // Lưu token vào database
            UserToken userToken = new UserToken();
            userToken.setUser(user);
            userToken.setToken(token);
            userTokenRepository.save(userToken);
            logger.info("Token saved to database for new user: {}", user.getEmail());

            return new AuthResponse(token, user);
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", registerRequest.getEmail(), e);
            throw e;
        }
    }

    public void logout(String token) {
        logger.info("Attempting to logout user with token");
        try {
            // Đánh dấu token không còn hợp lệ
            userTokenRepository.findValidToken(token)
                .ifPresent(userToken -> {
                    userToken.setValid(false);
                    userTokenRepository.save(userToken);
                    logger.info("Token marked as invalid for user: {}", userToken.getUser().getEmail());
                });
        } catch (Exception e) {
            logger.error("Logout failed", e);
            throw e;
        }
    }
} 