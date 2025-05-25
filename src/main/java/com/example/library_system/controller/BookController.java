package com.example.library_system.controller;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Book;
import com.example.library_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //Book with details (with author)
    @GetMapping("/book")
    public List<BookWithDetailsDTO> allBooks() {
        return bookService.allBooks();
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBookByTitle(@RequestParam String title) {
        List<BookDTO> books = bookService.searchBooksByTitle(title);

        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addBook(@RequestBody Book book) {
        Map<String, String> response = bookService.addBook(book);

        if (response.containsValue("success")) {
            return ResponseEntity.status(201).body(response);
        } else return ResponseEntity.status(500).body(response);

    }

}