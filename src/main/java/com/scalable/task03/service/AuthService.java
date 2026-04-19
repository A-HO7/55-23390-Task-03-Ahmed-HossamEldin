package com.scalable.task03.service;

import com.scalable.task03.dto.AuthResponse;
import com.scalable.task03.dto.LoginRequest;
import com.scalable.task03.dto.RegisterRequest;
import com.scalable.task03.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @org.springframework.beans.factory.annotation.Autowired
    private com.scalable.task03.config.JwtConfig jwtConfig;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // TODO: See Task 3 spec — AuthService.

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Email is taken");
        }

        com.scalable.task03.model.User user = new com.scalable.task03.model.User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                com.scalable.task03.model.Role.USER
        );
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, jwtConfig.getExpiration());
    }

    public AuthResponse login(LoginRequest request) {
        com.scalable.task03.model.User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, jwtConfig.getExpiration());
    }
}
