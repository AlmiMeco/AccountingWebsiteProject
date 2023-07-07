package com.cydeo.accounting_app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public record UserNotFound(String message, Throwable throwable, HttpStatus httpStatus) {

}
