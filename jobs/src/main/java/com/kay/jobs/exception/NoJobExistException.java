package com.kay.jobs.exception;

public class NoJobExistException extends RuntimeException{
    public NoJobExistException(String message) {
        super(message);
    }
}
