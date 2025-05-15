package com.example.bookshop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckEmailDto {

    private String email;
    private String code;
}
