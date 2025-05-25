package com.example.library_system.mapper;

import com.example.library_system.dto.BookDTO;
import com.example.library_system.entity.Book;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book){
        if(book == null){
            return null;
        }
        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        return dto;
    }

    public Book toEntity(BookDTO dto){
        if(dto == null){
            return null;
        }
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());
        return book;
    }

    public List<BookDTO> toDTOList(List<Book> books){
        if(books == null){
            return Collections.emptyList();
        }
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Book> toEntityList(List<BookDTO> DTOs){
        if(DTOs == null){
            return Collections.emptyList();
        }
        return DTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
