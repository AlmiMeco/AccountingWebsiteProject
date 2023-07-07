package com.cydeo.accounting_app.exception;

import org.springframework.http.ResponseEntity;

public class UserNotFoundExceptionHandler {

    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e){

    }

}
