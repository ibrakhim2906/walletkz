package com.ibrakhim2906.walletkz.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorTemplate> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest req
    ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ErrorTemplate error = new ErrorTemplate(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                status.getReasonPhrase(),
                ex.getReason(),
                req.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorTemplate> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " +err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorTemplate error = new ErrorTemplate(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                req.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorTemplate> handleGenericException(
            Exception ex,
            HttpServletRequest req
    ) {
        ErrorTemplate error = new ErrorTemplate(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unexpected Server Error",
                req.getRequestURI()
        );

        return ResponseEntity.internalServerError().body(error);
    }
}
