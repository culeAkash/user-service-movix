package com.movix.user.service.exceptions;


import com.movix.user.service.responses.ApiSubError;
import com.movix.user.service.responses.GenericApiResponse;
import com.movix.user.service.responses.GenericErrorResponse;
import jakarta.el.MethodNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        GenericErrorResponse errors = new GenericErrorResponse();
        errors.setStatus(HttpStatus.BAD_REQUEST);
        errors.setMessage("All fields do not satisfy the required parameters structure");
        errors.setSubErrors(new ArrayList<>());
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(err->{
//            LOGGER.debug(err.toString());
            String fieldName = ((FieldError)err).getField();
            Object rejectedValue =  ((FieldError)err).getRejectedValue();
            String message = err.getDefaultMessage();
            errors.getSubErrors().add(
                    ApiSubError.builder()
                            .message(message)
                            .field(fieldName)
                            .rejectedValue(rejectedValue)
                            .build()
            );
        });
        errors.setTimestamp(new Date());
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<GenericErrorResponse> duplicateEntryException(DuplicateEntryException exception) {
        GenericErrorResponse errorResponse = GenericErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);

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
