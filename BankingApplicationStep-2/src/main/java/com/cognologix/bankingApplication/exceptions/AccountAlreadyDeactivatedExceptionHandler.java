package com.cognologix.bankingApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountAlreadyDeactivatedExceptionHandler {
    @ExceptionHandler(AccountAlreadyDeactivatedException.class)
    public ResponseEntity<String> handleAccountAlreadyDeactivatedException(Exception exception){
        return new ResponseEntity<>("Exception : "+exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
