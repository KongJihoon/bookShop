package com.example.bookshop.user.service.impl;

import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.auth.service.AuthService;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.security.TokenProvider;
import com.example.bookshop.global.service.MailService;
import com.example.bookshop.global.service.RedisService;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
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
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {

        if (!userRepository.existsByEmail("test@example.com")) {


            SignUpUserDto.Request build = SignUpUserDto.Request.builder()
                    .loginId("testuser")
                    .password("1111@11")
                    .checkPassword("1111@11")
                    .email("test@example.com")
                    .nickname("testuser")
                    .birth(LocalDate.parse("2000-01-01"))
                    .phone("010-1111-1111")
                    .address("테스트 주소")
                    .build();


            userService.signUpUser(build);

            UserEntity user = userRepository.findByLoginId("testuser").orElseThrow();
            user.setEmailAuth();
            userRepository.save(user);

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


    @Test
    @DisplayName("로그인 유저")
    void loginUser() {
        // given

        UserDto userDto = authService.LoginUser("testuser", "1111@11");


        // when
        TokenDto token = authService.getToken(userDto);


        // then

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());


        assertEquals(tokenProvider.getUsernameFromToken(token.getAccessToken()), "testuser");
        assertEquals(redisService.getData("refresh_token:" + userDto.getLoginId()), token.getRefreshToken());

    }

    @Test
    @DisplayName("RefreshToken TTL 확인")
    void refreshTokenTTLTest() {

        // given

        UserDto userDto = authService.LoginUser("testuser", "1111@11");


        // when
        TokenDto token = authService.getToken(userDto);
        String key = "refresh_token:" + userDto.getLoginId();
        Long ttl = redisService.getExpire(key, TimeUnit.MILLISECONDS);
        assertTrue(ttl > 0 && ttl <= 3600000); // 1시간 이내
    }

    @Test
    void reIssueToken() {
        // given

        UserDto userDto = authService.LoginUser("testuser", "1111@11");
        TokenDto token = authService.getToken(userDto);



        // when

        TokenDto reIssuedToken = authService.reIssueToken(userDto.getLoginId(), token.getAccessToken(), token.getRefreshToken());


        // then

        assertNotNull(reIssuedToken.getAccessToken());
        assertNotNull(reIssuedToken.getRefreshToken());

        assertEquals(redisService.getData("refresh_token:" + userDto.getLoginId()), reIssuedToken.getRefreshToken());

    }

    @Test
    @DisplayName("토큰 갱신 예외 테스트")
    void reIssueTokenWithException() {
        // given
        UserDto userDto = authService.LoginUser("testuser", "1111@11");
        TokenDto token = authService.getToken(userDto);

        // Redis에 저장된 토큰 제거
        redisService.deleteData("refresh_token:" + userDto.getLoginId());

        // when & then
        assertThrows(CustomException.class, () -> {
            authService.reIssueToken(userDto.getLoginId(), token.getAccessToken(), token.getRefreshToken());
        });

    }





}
