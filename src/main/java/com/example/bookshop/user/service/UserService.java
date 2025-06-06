package com.example.bookshop.user.service;

import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.dto.EditUserInfo;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {

    ResultDto<SignUpUserDto.Response> signUpUser(SignUpUserDto.Request signUpUserDto);

    CheckDto checkEmail(String email);

    UserDto getUserInfo(Long userId);

    ResultDto<UserDto> editUserInfo(Long userId, EditUserInfo editUserInfo);

    CheckDto deleteUser(String loginId, String password);
}
