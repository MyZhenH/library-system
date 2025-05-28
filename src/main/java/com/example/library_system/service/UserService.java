package com.example.library_system.service;

import com.example.library_system.dto.UserDTO;
import com.example.library_system.entity.User;
import com.example.library_system.mapper.UserMapper;
import com.example.library_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();

        return userMapper.toDTOList(users);
    }

    public UserDTO getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmailContainingIgnoreCase(email);

        return user.map(userMapper::toDTO).orElse(null);
    }


    public Map<String, String> addUser(User user) {
        Map<String, String> response = new HashMap<>();

        try {
            user.setRegistrationDate(LocalDateTime.now().withNano(0));
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
