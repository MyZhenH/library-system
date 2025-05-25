package com.example.library_system.dto;

public class BookWithDetailsDTO {

    private BookDTO bookDTO;
    private AuthorDTO authorDTO;

    public BookWithDetailsDTO() {
    }

    public BookWithDetailsDTO(BookDTO bookDTO, AuthorDTO authorDTO) {
        this.bookDTO = bookDTO;
        this.authorDTO = authorDTO;
    }

    public BookDTO getBookDTO() {
        return bookDTO;
    }

    public void setBookDTO(BookDTO bookDTO) {
        this.bookDTO = bookDTO;
    }

    public AuthorDTO getAuthorDTO() {
        return authorDTO;
    }

    public void setAuthorDTO(AuthorDTO authorDTO) {
        this.authorDTO = authorDTO;
    }
}
