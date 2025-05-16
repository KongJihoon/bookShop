package com.example.bookshop.user.service.impl;

import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.service.MailService;
import com.example.bookshop.global.service.RedisService;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.example.bookshop.global.exception.ErrorCode.EXISTS_BY_EMAIL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {

        if (!userRepository.existsByEmail("test@example.com")) {


            SignUpUserDto.Request build = SignUpUserDto.Request.builder()
                    .loginId("testuser")
                    .password("1111@11")
                    .checkPassword("1111@11")
                    .email("test@example.com")
                    .nickname("testuser")
                    .birth(LocalDate.parse("1997-07-24"))
                    .phone("010-1111-1111")
                    .address("테스트 주소")
                    .build();
            userService.signUpUser(build);

        }

    }


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

    @Test
    @DisplayName("이메일 인증번호 전송")
    void sendAuthMail() {
        // given

        String email = "test@example.com";


        // when

        CheckDto checkDto = mailService.sendAuthMail(email);

        String code = redisService.getData("Email-Auth: " + email);

        // then


        assertNotNull(checkDto);
        assertTrue(checkDto.isSuccess());
        assertEquals("인증번호를 전송하였습니다.", checkDto.getMessage());

    }




}
