package com.taskmanager.system.service.impl;

import com.taskmanager.system.entity.User;
import com.taskmanager.system.enums.Role;
import com.taskmanager.system.exception.ResourceNotFoundException;
import com.taskmanager.system.repository.UserRepository;
import com.taskmanager.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User registerUser(String name, String email, String password) {
        // Check if user already exists
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new RuntimeException("User already exists");
                });

        // 2. Determine the role dynamically
        // If the database count is 0, this is the first user -> make them ADMIN
        Role assignedRole = (userRepository.count() == 0) ? Role.ADMIN : Role.USER;

        // Create user
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(assignedRole)
                .build();
        // Save user
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}
