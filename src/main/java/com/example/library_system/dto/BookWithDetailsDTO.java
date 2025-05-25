package com.example.library_system.dto;

public class BookWithDetailsDTO {
    private BookDTO book;
    private AuthorDTO author;

    public BookWithDetailsDTO() {
    }

    public BookWithDetailsDTO(BookDTO book, AuthorDTO author) {
        this.book = book;
        this.author = author;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}
