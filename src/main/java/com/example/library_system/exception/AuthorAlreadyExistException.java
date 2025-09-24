package com.example.library_system.exception;

public class AuthorAlreadyExistException extends RuntimeException{
    public AuthorAlreadyExistException (String message){
        super (message);
    }
}
