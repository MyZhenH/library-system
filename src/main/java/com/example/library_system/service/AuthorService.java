package com.example.library_system.service;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.mapper.AuthorMapper;
import com.example.library_system.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper){
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors(){
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDTOList(authors);
    }

    public List<AuthorDTO> getAuthorByLastName(String lastName){
        List<Author> author = authorRepository.findByLastNameContainingIgnoreCase(lastName);
        return authorMapper.toDTOList(author);
    }

    public Map<String, String> addAuthor(Author author) {
        Map<String, String> response = new HashMap<>();

        try{
            authorRepository.save(author);
            response.put("status", "success");
            response.put("message", "Author added " + author.toString());
            return response;

        }catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
            return response;
        }
    }


}



