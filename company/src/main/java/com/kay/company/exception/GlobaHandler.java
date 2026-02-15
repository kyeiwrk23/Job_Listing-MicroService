package com.kay.company.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobaHandler {

    @ExceptionHandler(CompanyExistException.class)
    public ResponseEntity<String> handlerCompanyExistException(CompanyExistException ex){
        String msg = String.format("Company %s Already Exist!!!",ex.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoCompanyExistException.class)
    public ResponseEntity<String> handlerCompanyExistException(NoCompanyExistException ex){
        String msg = String.format("Company %s doesn't  Exist",ex.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }


}
