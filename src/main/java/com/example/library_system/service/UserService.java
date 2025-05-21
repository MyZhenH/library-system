package com.example.library_system.service;

import com.example.library_system.entity.User;
import com.example.library_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> getUserEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Map<String, String> addUser(User user) {
        Map<String, String> response = new HashMap<>();

        try {
            userRepository.save(user);
            response.put("status", "success");
            response.put("message", "User added: " + user.toString());
            return response;
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
            return response;
        }
    }



}
