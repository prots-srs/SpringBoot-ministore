package com.protsdev.ministore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.protsdev.ministore.localize.LocalizeService;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LocalizeService localizeService;

    // @ExceptionHandler(Throwable.class)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalError(final Throwable throwable, final Model model) {
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("message", errorMessage);

        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String badRequest(final Throwable throwable, final Model model) {
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("message", errorMessage);
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noFoundError(final Model model) {
        String message = localizeService.getMessage("errors.notfound");
        model.addAttribute("message", message);
        return "error";
    }

}
