package com.example.library_system.service;

import com.example.library_system.utils.Sanitizer;
import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.exception.*;
import com.example.library_system.mapper.AuthorMapper;
import com.example.library_system.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    //Hämta alla författare
    public List<AuthorDTO> getAllAuthors(){
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDTOList(authors);
    }

    //Sök författare efter efternamn
    public List<AuthorDTO> getAuthorByLastName(String lastName){
        List<Author> author = authorRepository.findByLastNameContainingIgnoreCase(lastName);

        if(author.isEmpty()){
            throw new AuthorNotFoundException("Author not found! " + lastName);
        }
        return authorMapper.toDTOList(author);
    }

    //Lägga till författare
    public Map<String, String> addAuthor(Author author) {
        Map<String, String> response = new HashMap<>();

        String sanitizedFirstName = Sanitizer.sanitize(author.getFirstName());
        String sanitizedLastName = Sanitizer.sanitize(author.getLastName());
        String sanitizedNationality = Sanitizer.sanitize(author.getNationality());

        if (sanitizedFirstName == null || sanitizedFirstName.isBlank()) {
            throw new BadRequestException("First name is required");
        }

        if (sanitizedLastName == null || sanitizedLastName.isBlank()) {
           throw new BadRequestException("Last name is required");
        }

        // Kontrollera så att födelseåret är ett positivt heltal och inte i framtiden
        if (author.getBirthYear() <= 0 || author.getBirthYear() > LocalDate.now().getYear()) {
            throw new BadRequestException("Invalid birth year. Birth year is required and cannot be in the future or negative");
        }

        if (sanitizedNationality == null || sanitizedNationality.isBlank()) {
            throw new BadRequestException("Nationality is required");
        }
        List<Author> authorExists = authorRepository.findByFirstNameAndLastNameAndBirthYearAndNationality(
                sanitizedFirstName,
                sanitizedLastName,
                author.getBirthYear(),
                sanitizedNationality);

        if(!authorExists.isEmpty()){
            throw new AuthorAlreadyExistException("Author already exist in the registry");
        }

        author.setFirstName(sanitizedFirstName);
        author.setLastName(sanitizedLastName);
        author.setNationality(sanitizedNationality);

        authorRepository.save(author);
        response.put("status", "success");
        response.put("message", "Author added " + author.toString());
        return response;

    }


}



