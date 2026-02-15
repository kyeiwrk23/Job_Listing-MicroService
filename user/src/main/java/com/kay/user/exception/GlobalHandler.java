package com.kay.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(NoUserExistException.class)
    public ResponseEntity<String> handleNoUserException(NoUserExistException ex){
        String msg = String.format("User Id %s doesn't exist!!!",ex.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<String> handleUserExistException(UserExistException ex){
        String msg = String.format("User  %s already exist!!!",ex.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleNoUserCreatedException(ApiException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
