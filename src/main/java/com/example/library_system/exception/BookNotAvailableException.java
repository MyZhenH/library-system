package com.example.library_system.exception;

public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(String message){
        super(message);
    }
}
