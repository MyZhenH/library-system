package com.example.library_system.service;

import com.example.library_system.exception.ForbiddenException;
import com.example.library_system.utils.Sanitizer;
import com.example.library_system.dto.UserDTO;
import com.example.library_system.entity.User;
import com.example.library_system.exception.PasswordException;
import com.example.library_system.mapper.UserMapper;
import com.example.library_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    SecurityService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getAllUsers(){

        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmailContainingIgnoreCase(email);

        return user.map(userMapper::toDTO).orElse(null);
    }

    public Map<String, String> addUser(User user) {
        Map<String, String> response = new HashMap<>();

        //Förhindra XSS genom sanera användarens indata
        String sanitizedFirstName = Sanitizer.sanitize(user.getFirstName());
        String sanitizedLastName = Sanitizer.sanitize(user.getLastName());
        String sanitizedEmail = Sanitizer.sanitize(user.getEmail());

        user.setFirstName(sanitizedFirstName);
        user.setLastName(sanitizedLastName);
        user.setEmail(sanitizedEmail);

        // Validering - Kontrollera om användaren redan finns
        Optional<User> existingUser = userRepository.findByEmailContainingIgnoreCase(user.getEmail());
        if (existingUser.isPresent()) {
            response.put("status", "error");
            response.put("message", "User already exists.");
            return response;
        }

        // Validering - Kontrollera om lösenorden uppnå kraven
        passwordValidation(user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setRegistrationDate(LocalDateTime.now().withNano(0));

        userRepository.save(user);
        response.put("status", "success");
        response.put("message", "Registration successful");
        return response;

    }


        public void passwordValidation(String password) {
            if (password == null || password.isEmpty()) {
                throw new PasswordException("Password cannot be empty");
            }
            if (password.length() < 8) {
                throw new PasswordException("Password must be at least 8 characters long");
            }
            if (!password.matches(".*[A-ZÅÄÖ].*")) {
                throw new PasswordException("Password must contain at least one uppercase letter");
            }
            if (!password.matches(".*[a-ö].*")) {
                throw new PasswordException("Password must contain at least one lowercase letter");
            }
            if (!password.matches(".*[0-9].*")) {
                throw new PasswordException("Password must contain at least one number");

        }
}


    //TEST
    public void addTestUsers() {
        createUser("UserTest", "UserTest", "userTest@email.com", "userPassword", "ROLE_USER");
        createUser("AdminTest", "AdminTest", "adminTest@email.com", "adminPassword", "ROLE_ADMIN");
    }

    private void createUser(String firstName, String lastName, String email, String password, String role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        user.setRole(role);
        user.setRegistrationDate(LocalDateTime.now().withNano(0));

        userRepository.save(user);

    }




}
