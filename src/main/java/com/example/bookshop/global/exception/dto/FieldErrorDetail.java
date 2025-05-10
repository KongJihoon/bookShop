package com.example.bookshop.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FieldErrorDetail {

    private final String field;
    private final String code;
    private final String Message;


}
