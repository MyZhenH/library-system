package com.example.library_system.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException (String message){
        super (message);
    }
}
