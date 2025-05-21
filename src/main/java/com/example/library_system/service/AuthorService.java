package com.example.library_system.service;

import com.example.library_system.entity.Author;
import com.example.library_system.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;


    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    public List<Author> getAuthorByLastName(String lastName){
        return authorRepository.findByLastName(lastName);
    }

    public Map<String, String> addAuthor (Author author) {
        Map<String, String> response = new HashMap<>();

            try {
                authorRepository.save(author);
            response.put("status", "success");
            response.put("message", "Author added " + author.toString());
            return response;

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
            return response;


        }
    }


}



