package com.example.library_system.service;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import com.example.library_system.mapper.BookWithDetailsMapper;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookWithDetailsMapper bookWithDetailsMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       BookWithDetailsMapper bookWithDetailsMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookWithDetailsMapper = bookWithDetailsMapper;
    }

    public List<BookWithDetailsDTO> getAllBooksWithDetails() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookWithDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookWithDetailsDTO> searchBooks(String title, String name){
        List<Book> books;

        if(title != null && !title.isEmpty()){
            books = bookRepository.findByTitleContainingIgnoreCase(title);

        } else if(name != null && !name.isEmpty()){
            books = bookRepository.findByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase
                    (name, name);

        } else{
            books = Collections.emptyList();
        }

        return books.stream()
                .map(bookWithDetailsMapper::toDTO)
                .collect(Collectors.toList());

    }

    public Map<String, String> addBook(BookDTO bookDTO) {
        Map<String, String> response = new HashMap<>();

        try {
            if (bookDTO.getAuthorId() == null) {
                response.put("status", "error");
                response.put("message", "enter author ID");
                return response;
            }
            Author author = authorRepository.findById(bookDTO.getAuthorId()).orElse(null);

            if (author == null) {
                response.put("status", "error");
                response.put("message", "Author not found.");
                return response;
            }

            Book book = new Book();
            book.setTitle(bookDTO.getTitle());
            book.setPublicationYear(bookDTO.getPublicationYear());
            book.setAvailableCopies(bookDTO.getAvailableCopies());
            book.setTotalCopies(bookDTO.getTotalCopies());
            book.setAuthor(author);

            bookRepository.save(book);
            response.put("status", "success");
            response.put("message", "Book added: " + book.getTitle());

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
        }
        return response;

    }
}




