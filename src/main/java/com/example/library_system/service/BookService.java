package com.example.library_system.service;

import com.example.library_system.entity.Book;
import com.example.library_system.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByTitle(String title){
        return bookRepository.findByTitle(title);
    }

    public Map<String, String> addBook(Book book){
        Map<String, String> response = new HashMap<>();

        try {
            bookRepository.save(book);
            response.put("status", "success");
            response.put("message", "Book added: " + book.toString());
            return response;
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
            return response;
        }

    }



}
