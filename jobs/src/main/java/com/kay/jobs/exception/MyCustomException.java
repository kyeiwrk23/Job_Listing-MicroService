package com.kay.jobs.exception;


import org.springframework.http.HttpStatusCode;

public class MyCustomException extends RuntimeException {

    HttpStatusCode httpStatusCode;
    public MyCustomException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
