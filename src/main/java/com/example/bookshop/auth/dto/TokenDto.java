package com.example.bookshop.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {


    private String loginId;

    private String accessToken;

    private String refreshToken;

}
