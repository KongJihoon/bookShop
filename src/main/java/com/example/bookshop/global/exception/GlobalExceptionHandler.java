package com.example.bookshop.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        log.error("CustomException 발생 : {} " , e.getErrorMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getErrorCode().statusCode())
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build();


        return new ResponseEntity<>(errorResponse, e.getErrorCode().getStatus());

    }
}
