package com.example.bookshop.global.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;



}
