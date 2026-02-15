package com.kay.jobs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NoJobExistException.class)
    public ResponseEntity<String> handlerNoJobException(NoJobExistException ex){
        String str = String.format("Job with Id %s not available ",ex.getMessage());
        return new ResponseEntity<>(str, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MyCustomException.class)
    public ResponseEntity<String> handlerMyCustomException(MyCustomException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.httpStatusCode);
    }
}
