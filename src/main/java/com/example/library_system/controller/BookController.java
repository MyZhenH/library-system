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


    @GetMapping
    public ResponseEntity<List<BookWithDetailsDTO>> getAllBooks(){
        List<BookWithDetailsDTO> books = bookService.getAllBooksWithDetails();

        if(books.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookWithDetailsDTO>> searchBooks(@RequestParam(required = false) String title,
                                                                @RequestParam(required = false) String author) {
        List<BookWithDetailsDTO> books = bookService.searchBooks(title, author);

        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addBook(@RequestBody BookDTO bookDTO) {
        Map<String, String> response = bookService.addBook(bookDTO);

        if (response.containsValue("success")) {
            return ResponseEntity.status(201).body(response);
        } else return ResponseEntity.status(500).body(response);
    }

}