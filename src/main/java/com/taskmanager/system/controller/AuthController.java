package com.taskmanager.system.controller;

import com.taskmanager.system.dto.request.LoginRequestDTO;
import com.taskmanager.system.dto.request.SignUpRequestDTO;
import com.taskmanager.system.dto.response.CustomApiResponse;
import com.taskmanager.system.dto.response.UserResponseDTO;
import com.taskmanager.system.entity.User;
import com.taskmanager.system.exception.BadRequestException;
import com.taskmanager.system.security.JwtUtil;
import com.taskmanager.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Auth APIs", description = "Authentication operations")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Register
    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<UserResponseDTO>> register(@Valid @RequestBody SignUpRequestDTO request){

        User savedUser = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        UserResponseDTO userData = UserResponseDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .build();

        CustomApiResponse<UserResponseDTO> response = CustomApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User registered successfully")
                .data(userData)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login
    @Operation(summary = "Login and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<String>> login(@Valid @RequestBody LoginRequestDTO request){
        // Get user
        User user = userService.getUserByEmail(request.getEmail());

        // Check Password
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid credentials");
        }

        // Generate Token
        String token = jwtUtil.generateToken(user.getEmail());

        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(true)
                .message("Login successful")
                .data(token)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
