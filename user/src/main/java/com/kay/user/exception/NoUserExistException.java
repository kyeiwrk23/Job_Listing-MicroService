package com.kay.user.exception;


public class NoUserExistException extends RuntimeException{
    public NoUserExistException(String message) {
        super(message);
    }

}
