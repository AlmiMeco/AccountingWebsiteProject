package com.cydeo.accounting_app.exception;

import com.cydeo.accounting_app.annotation.DefaultExceptionMessage;
import com.cydeo.accounting_app.dto.DefaultExceptionMessageDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception    .class, RuntimeException.class, Throwable.class, BadCredentialsException.class})
    public String genericException(Throwable e, HandlerMethod handlerMethod, Model model) {

        Optional<DefaultExceptionMessageDTO> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) {
            model.addAttribute("message", defaultMessage.get().getMessage());
            return "error";
        }
        model.addAttribute("message","Something went wrong!");
        return "error";
    }


    private Optional<DefaultExceptionMessageDTO> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDTO defaultExceptionMessageDTO = DefaultExceptionMessageDTO
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDTO);
        }
        return Optional.empty();
    }

}
