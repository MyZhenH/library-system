package com.example.library_system.exception;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String message){
        super(message);
    }
}
