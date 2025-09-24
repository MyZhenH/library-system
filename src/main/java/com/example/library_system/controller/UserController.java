package com.example.library_system.controller;

import com.example.library_system.dto.UserDTO;
import com.example.library_system.entity.User;
import com.example.library_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //Hämta alla användare
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List <UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Sök användare efter email
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> searchUserByEmail(@PathVariable String email){
        UserDTO userDTO = userService.getUserByEmail(email);

        if (userDTO != null){
            return ResponseEntity.ok(userDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //Registrera sig
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody User user){
        Map<String, String> response = userService.addUser(user);

        if("success".equals((response.get("status")))){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }


}
