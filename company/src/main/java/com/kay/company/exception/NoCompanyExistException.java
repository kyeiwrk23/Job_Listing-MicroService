package com.kay.company.exception;

public class NoCompanyExistException extends RuntimeException{
    public NoCompanyExistException(String message) {
        super(message);
    }
}
