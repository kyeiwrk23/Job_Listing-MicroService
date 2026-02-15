package com.kay.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NoReviewExistException.class)
    public ResponseEntity<String> handlerNoReviewExistException(NoReviewExistException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
