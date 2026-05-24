package com.taskmanager.system.service;

import com.taskmanager.system.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User registerUser(String name, String email, String password);
    User getUserByEmail(String email);
}
