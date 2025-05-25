package com.example.library_system.controller;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<AuthorDTO>> searchAuthorByLastName(@PathVariable String lastName) {
        List<AuthorDTO> author = authorService.getAuthorByLastName(lastName);

        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity <Map<String, String>> addAuthor (@RequestBody Author author) {
        Map<String, String> response = authorService.addAuthor(author);

        if (response.containsValue("success")) {
            return ResponseEntity.status(201).body(response);
        } else return ResponseEntity.status(500).body(response);
    }


}
