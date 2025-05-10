package com.example.bookshop.global.exception.dto;

import com.example.bookshop.global.exception.ErrorCode;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;
    private List<FieldErrorDetail> details;



    public static ErrorResponse of(ErrorCode errorCode, List<FieldErrorDetail> details) {


        return ErrorResponse.builder()
                .statusCode(errorCode.statusCode())
                .errorCode(errorCode)
                .errorMessage(errorCode.getMessage())
                .details(details)
                .build();

    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.of(errorCode, List.of());
    }

}
