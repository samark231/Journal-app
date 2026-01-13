package com.samar.Journal_app.exception;

public class JournalNotFoundException extends RuntimeException{
    public JournalNotFoundException(String message){
        super(message);
    }
}
