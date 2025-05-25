package com.example.library_system.service;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.entity.Book;
import com.example.library_system.mapper.BookMapper;
import com.example.library_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    //public List<Book> getAllBooks(){
      //  return bookRepository.findAll();
    //}

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
