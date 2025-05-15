package com.example.bookshop.user.service.impl;

import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.example.bookshop.global.exception.ErrorCode.EXISTS_BY_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    @DisplayName("User_SignUp_Success")
    void signUpUser() {
        // given
        SignUpUserDto.Request request = SignUpUserDto.Request.builder()
                .loginId("")
                .password("")
                .checkPassword("")
                .email("")
                .nickname("")
                .birth(LocalDate.parse(""))
                .phone("")
                .address("")
                .build();


        // when
        ResultDto<SignUpUserDto.Response> responseResultDto = userService.signUpUser(request);

        // then
        assertEquals("회원가입에 성공하였습니다.", responseResultDto.getMessage());
    }

    @Test
    @DisplayName("User_SignUp_Fail_중복_이메일")
    void signUpFail_duplicateEmail() {
        // given
        SignUpUserDto.Request request = SignUpUserDto.Request.builder()
                .loginId("")
                .password("")
                .checkPassword("")
                .email("")
                .nickname("")
                .birth(LocalDate.parse(""))
                .phone("")
                .address("")
                .build();

        // 1차 회원가입 → 정상 가입
        userService.signUpUser(request);

        // 2차 회원가입 → 같은 이메일로 재시도
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signUpUser(request);
        });

        // then
        assertEquals(EXISTS_BY_EMAIL.getMessage(), exception.getMessage());
    }





}