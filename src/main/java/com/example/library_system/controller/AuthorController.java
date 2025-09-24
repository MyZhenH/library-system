package com.example.library_system.controller;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.service.AuthorService;
import com.example.library_system.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @Autowired
    SecurityService securityService;

    //Hämta alla författare
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> author = authorService.getAllAuthors();

        if(author.isEmpty()){
            return ResponseEntity.ok().body(Collections.emptyList()); //Returnera en tom lista
        }
        return ResponseEntity.ok(author);
    }

    //Hämta författare efter efternamn
    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<AuthorDTO>> searchAuthorByLastName(@PathVariable String lastName) {
        List<AuthorDTO> author = authorService.getAuthorByLastName(lastName);

        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    //Lägg till författare
    @PostMapping
    public ResponseEntity <Map<String, String>> addAuthor (@RequestBody Author author) {
        Map<String, String> response = authorService.addAuthor(author);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
