package com.cydeo.accounting_app.exception;

import com.cydeo.accounting_app.annotation.DefaultExceptionMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception    .class, RuntimeException.class, Throwable.class})
    public String genericException( HandlerMethod handlerMethod) {
        return getMessageFromAnnotation(handlerMethod.getMethod());
    }
    private String getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        return defaultExceptionMessage.defaultMessage();
    }

}
