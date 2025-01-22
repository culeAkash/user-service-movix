package com.movix.user.service.exceptions;


import com.movix.user.service.responses.GenericApiResponse;
import jakarta.el.MethodNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<GenericApiResponse> handleGenericException(GenericException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(GenericApiResponse.builder()
                        .message(exception.getMessage())
                        .success(false)
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericApiResponse> notFoundException(ResourceNotFoundException exception) {
        String message = exception.getMessage();
        GenericApiResponse apiResponse = GenericApiResponse.builder()
                .message(message)
                .success(false)
                .build();
        return new ResponseEntity<GenericApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<GenericApiResponse> handlerIOException(IOException e) {
        GenericApiResponse response = new GenericApiResponse(e.getMessage(), false);
        return new ResponseEntity<GenericApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
