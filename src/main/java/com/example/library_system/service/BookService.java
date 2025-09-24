package com.example.library_system.service;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import com.example.library_system.exception.BadRequestException;
import com.example.library_system.mapper.BookWithDetailsMapper;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.utils.Sanitizer;
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

        String sanitizedTitle = Sanitizer.sanitize(bookDTO.getTitle());

        if (sanitizedTitle == null || sanitizedTitle.isBlank()) {
            throw new BadRequestException("tile is required");
        }

        if (bookDTO.getAuthorId() == null) {
            throw new BadRequestException("Author ID is required");
        }

        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElse(null);
        if (author == null) {
            throw new BadRequestException("Author not found");
        }

        if (bookDTO.getPublicationYear() <= 0) {
            throw new BadRequestException("Invalid publication year");
        }

        if (bookDTO.getAvailableCopies() <= 0 || bookDTO.getTotalCopies() <= 0) {
            throw new BadRequestException("Both available copies and total copies must be specified");
        }

        Book book = new Book();
        book.setTitle(sanitizedTitle);
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAuthor(author);

        bookRepository.save(book);
        response.put("status", "success");
        response.put("message", "Book added: " + book.getTitle());
        return response;

    }
}




