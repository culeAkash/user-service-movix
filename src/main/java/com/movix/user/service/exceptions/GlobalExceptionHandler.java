package com.movix.user.service.exceptions;


import com.movix.user.service.responses.ApiSubError;
import com.movix.user.service.responses.GenericErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
    public ResponseEntity<GenericErrorResponse> handleGenericException(GenericException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericErrorResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .timestamp(new Date())
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> notFoundException(ResourceNotFoundException exception) {
        String message = exception.getMessage();
        GenericErrorResponse errorResponse = GenericErrorResponse.builder()
                .message(message)
                .status(HttpStatus.NOT_FOUND)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<GenericErrorResponse>(errorResponse, errorResponse.getStatus());
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        GenericErrorResponse errorResponse = GenericErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .build();
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(new Date())
                .build()
        );
    }



    @ExceptionHandler(IOException.class)
    public ResponseEntity<GenericErrorResponse> handlerIOException(IOException e) {
        GenericErrorResponse response = new GenericErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,new Date(),e.getMessage(),null);
        return new ResponseEntity<GenericErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<GenericErrorResponse> handleAllException(Exception ex) {
        LOGGER.error(ex.getMessage());
        GenericErrorResponse errorResponse = GenericErrorResponse.builder()
                .message("Something went wrong!")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(new Date())
                .subErrors(new ArrayList<>())
                .build();
//        Map<String, String> errors = new HashMap<>();
//        errors.put("error", ex.getMessage());
        errorResponse.getSubErrors().add(
                ApiSubError.builder()
                        .message(ex.getMessage())
                        .build()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
