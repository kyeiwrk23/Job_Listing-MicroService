package com.kay.company.exception;

public class CompanyExistException extends RuntimeException{
    public CompanyExistException(String message) {
        super(message);
    }
}
