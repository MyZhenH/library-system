package com.example.library_system.service;

import com.example.library_system.entity.User;
import com.example.library_system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataInitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData(){
        if(userRepository.findByEmailContainingIgnoreCase("user@test.com").isEmpty()){
            User user = new User();
            user.setFirstName("User");
            user.setLastName("UserTest");
            user.setEmail("user@test.com");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole("ROLE_USER");
            user.setRegistrationDate(LocalDateTime.now().withNano(0));
            userRepository.save(user);
        }

        if(userRepository.findByEmailContainingIgnoreCase("admin@test.com").isEmpty()){
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("AdminTest");
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setRegistrationDate(LocalDateTime.now().withNano(0));
            userRepository.save(admin);
        }
        System.out.println("Admin är skapad");
        System.out.println("User är skapad");
    }
}
