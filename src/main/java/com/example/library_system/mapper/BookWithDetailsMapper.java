package com.example.library_system.mapper;

import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BookWithDetailsMapper {
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    @Autowired
    public BookWithDetailsMapper(BookMapper bookMapper, AuthorMapper authorMapper) {
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    public BookWithDetailsDTO toDTO(Book book){
        if(book == null){
            return null;
        }

        BookWithDetailsDTO dto = new BookWithDetailsDTO();
        dto.setBook(bookMapper.toDTO(book));

        if(book.getAuthor() != null){
            dto.setAuthor(authorMapper.toDTO(book.getAuthor()));

    }
        return dto;
    }

}
