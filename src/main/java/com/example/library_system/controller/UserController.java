package com.example.library_system.controller;

import com.example.library_system.dto.UserDTO;
import com.example.library_system.entity.User;
import com.example.library_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List <UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> searchUserByEmail(@PathVariable String email){
        UserDTO userDTO = userService.getUserByEmail(email);

        if (userDTO != null){
            return ResponseEntity.ok(userDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addUser(@RequestBody User user){
        Map<String, String> response = userService.addUser(user);

        if(response.containsValue("success")){
            return ResponseEntity.status(201).body(response);
        }else return ResponseEntity.status(500).body(response);
    }

}
