package com.example.library_system.mapper;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.dto.BookWithDetailsDTO;
import com.example.library_system.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BookWithDetailsMapper {
    private final AuthorMapper authorMapper;

    @Autowired
    public BookWithDetailsMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public BookWithDetailsDTO toDTO(Book book){
        if(book == null){
            return null;
        }

        BookWithDetailsDTO dto = new BookWithDetailsDTO();
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setTotalCopies(book.getTotalCopies());


        if(book.getAuthor() != null){
            AuthorDTO authorDTO = authorMapper.toDTO(book.getAuthor());
            String fullName = authorDTO.getFirstName() + " " + authorDTO.getLastName();
            dto.setAuthor(fullName);
    }
        return dto;
    }

}
