package com.protsdev.ministore.controllers;

import java.net.http.HttpRequest;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.protsdev.ministore.localize.LocalizeService;

@ControllerAdvice
public class GlobalExceptionHandler {
    // private static Logger logger =
    // LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @Autowired
    private LocalizeService LocalizeService;

    // @ExceptionHandler(Throwable.class)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalError(final Throwable throwable, final Model model) {
        // logger.error("Exception during execution of SpringSecurity application",
        // throwable);

        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("message", errorMessage);
        // System.out.println("-->> internalError: " + errorMessage);

        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String badRequest(final Throwable throwable, final Model model) {
        // String message = LocalizeService.getLocalizedMessage("exception.badRequest");
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("message", errorMessage);
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noFoundError(final Model model) {
        String message = LocalizeService.getLocalizedMessage("errors.notfound");
        model.addAttribute("message", message);
        return "error";
    }

}
