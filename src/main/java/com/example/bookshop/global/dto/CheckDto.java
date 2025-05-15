package com.example.bookshop.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CheckDto {

    private boolean success;
    private String message;

}
