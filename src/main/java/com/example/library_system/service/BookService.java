package com.example.library_system.service;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Book;
import com.example.library_system.mapper.AuthorMapper;
import com.example.library_system.mapper.BookMapper;
import com.example.library_system.mapper.BookWithDetailsMapper;
import com.example.library_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final BookWithDetailsMapper bookWithDetailsMapper;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper,
                       AuthorMapper authorMapper, BookWithDetailsMapper bookWithDetailsMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.bookWithDetailsMapper = bookWithDetailsMapper;
    }

    public List<BookWithDetailsDTO> getAllBooksWithDetails() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookWithDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<BookDTO> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDTOList(books);

    }

    public List<BookDTO> searchBooksByTitle(String title){
        List<Book> book = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookMapper.toDTOList(book);
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
