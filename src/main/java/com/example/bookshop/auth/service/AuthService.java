package com.example.bookshop.auth.service;

import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {


    UserDto LoginUser(String loginId, String password);

    TokenDto getToken(UserDto userDto);

    TokenDto reIssueToken(String loginId, String accessToken, String refreshToken);
}
