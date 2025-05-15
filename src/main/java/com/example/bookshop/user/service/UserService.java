package com.example.bookshop.user.service;

import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.dto.SignUpUserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResultDto<SignUpUserDto.Response> signUpUser(SignUpUserDto.Request signUpUserDto);

    CheckDto checkEmail(String email);
}
