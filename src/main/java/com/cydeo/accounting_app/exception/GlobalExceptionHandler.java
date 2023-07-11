package com.cydeo.accounting_app.exception;

import com.cydeo.accounting_app.annotation.DefaultExceptionMessage;
import com.cydeo.accounting_app.dto.DefaultExceptionMessageDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public String categoryNotFoundException(CategoryNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String productNotFoundException(ProductNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }
    

    @ExceptionHandler(ClientVendorNotFoundException.class)
    public String clientVendorNotFoundException(ClientVendorNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public String invoiceNotFoundException(InvoiceNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }

    @ExceptionHandler(InvoiceProductNotFoundException.class)
    public String invoiceProductNotFoundException(InvoiceProductNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public String companyNotFoundException(CompanyNotFoundException exception, Model model) {
        String message = exception.getMessage();
        model.addAttribute("message", message);
        return "error";
    }

    @ExceptionHandler({Throwable.class})
    public String genericException(Throwable exception, HandlerMethod handlerMethod, Model model) {
        exception.printStackTrace();
        String message = "Something went wrong!";
        Optional<DefaultExceptionMessageDTO> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent()) {
            message = defaultMessage.get().getMessage();
        } else if (!exception.getMessage().isEmpty()) {
            message = exception.getMessage();
        }
        model.addAttribute("message", message);
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
