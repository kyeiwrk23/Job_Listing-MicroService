package com.kay.submitApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NoSubmitResourceExistException.class)
    public ResponseEntity<String> handlerNoApplicationExistException(NoSubmitResourceExistException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubmitResourceExistException.class)
    public ResponseEntity<String> handlerApplicationExistException(SubmitResourceExistException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
