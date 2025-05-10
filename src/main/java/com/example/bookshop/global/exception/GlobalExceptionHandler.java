package com.example.bookshop.global.exception;

import com.example.bookshop.global.exception.dto.ErrorResponse;
import com.example.bookshop.global.exception.dto.FieldErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<String, ErrorCode> CODE_MAP = Map.of(
            "NotBlank", ErrorCode.INVALID_INPUT,
            "NotNull", ErrorCode.INVALID_INPUT,
            "Pattern", ErrorCode.INVALID_PATTERN,
            "Positive", ErrorCode.INVALID_PATTERN,
            "Email", ErrorCode.INVALID_PATTERN,
            "Past", ErrorCode.PAST_BIRTHDAY
    );

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        log.error("CustomException 발생 : {} " , e.getErrorMessage());




        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()),
                e.getErrorCode().getStatus());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("MethodArgumentNotValidException 발생 : {}", e.getMessage());

        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldErrorDetail.of(
                        error.getField(),
                        error.getCode(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        String firstCode = Optional.ofNullable(e.getBindingResult().getFieldErrors().get(0).getCode())
                .orElse("NotBlank");
        ErrorCode errorCode = map(firstCode);

        return new ResponseEntity<>(ErrorResponse.of(errorCode, details),errorCode.getStatus());
    }


    private static ErrorCode map(String validationCode) {
        return CODE_MAP.getOrDefault(validationCode, ErrorCode.INVALID_INPUT);
    }

}
