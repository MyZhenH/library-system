package com.example.library_system.controller;

import com.example.library_system.repository.UserRepository;
import com.example.library_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String userEndpoint(){
        return "Hello user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminEndpoint(){
        return "Hello admin";
    }

    @GetMapping("/home")
    public String homepage(){
        return "Welcome to Library Homepage";
    }




}
