package com.listaai.list.adapter.inbound.web.handler;

import com.listaai.list.application.exception.ShoppingListNotFoundException;
import com.listaai.list.domain.exception.item.ItemAlreadyAddedException;
import com.listaai.list.domain.exception.item.ItemNotFoundException;
import com.listaai.list.domain.exception.list.EmptyShoppingListCannotBeSharedException;
import com.listaai.list.domain.exception.list.ShoppingListWithoutParticipantsCannotBeSharedException;
import com.listaai.list.domain.exception.participant.ParticipantAlreadyAddedException;
import com.listaai.list.domain.exception.participant.ParticipantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ErrorResponseFactory errorResponseFactory;

    @ExceptionHandler({ItemNotFoundException.class, ParticipantNotFoundException.class, ShoppingListNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, ex.getMessage(), request));
    }

    @ExceptionHandler({
            ItemAlreadyAddedException.class,
            EmptyShoppingListCannotBeSharedException.class,
            ShoppingListWithoutParticipantsCannotBeSharedException.class,
            ParticipantAlreadyAddedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, message, request));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, message, request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, "Malformed or unreadable JSON request body", request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        log.error("Unexpected error on [{}] {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(status)
                .body(errorResponseFactory.build(status, "An unexpected error occurred", request));
    }
}
