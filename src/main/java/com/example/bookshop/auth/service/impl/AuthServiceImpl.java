package com.example.bookshop.auth.service.impl;

import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.auth.service.AuthService;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.security.TokenProvider;
import com.example.bookshop.global.service.RedisService;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.type.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bookshop.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final RedisService redisService;

    /**
     * 유저 로그인
     */
    @Override
    @Transactional
    public UserDto LoginUser(String loginId, String password) {



        log.info("[로그인 시도] loginId: {}", loginId);

        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("[로그인 실패]: 비밀번호 불일치: {}", loginId);
            throw new CustomException(PASSWORD_MISMATCH);
        }

        if (!user.isEmailAuth()) {

            log.warn("[로그인 실패] 이메일 인증 에러: {}", loginId);
            throw new CustomException(PROCEED_EMAIL_AUTH);
        }


        log.info("[로그인 성공] loginId: {}", loginId);
        return UserDto.fromEntity(user);
    }


    /**
     * JWT 토큰 발급
     */
    @Override
    public TokenDto getToken(UserDto userDto) {

        log.info("토큰 생성 시작");


        String accessToken = tokenProvider.createAccessToken(userDto.getLoginId(), userDto.getEmail(), UserType.valueOf(userDto.getUserType()));

        log.info("[토큰 생성 완료] AccessToken");

        String refreshToken = tokenProvider.createRefreshToken(userDto.getLoginId());
        log.info("[토큰 생성 완료] RefreshToken");


        return new TokenDto(userDto.getLoginId(), accessToken, refreshToken);
    }


    /**
     * 토큰 재발급
     */
    @Override
    public TokenDto reIssueToken(String loginId, String accessToken, String refreshToken) {

        log.info("토큰 재발급 시작");


        // 1. RefreshToken 안의 로그인 아이디 추출 후 아이디 검증

        if (refreshToken.isBlank()) {
            throw new CustomException(NOT_FOUND_TOKEN);
        }

        String username = tokenProvider.parseToken(refreshToken).getSubject();

        if (!username.equals(loginId)) {
            log.warn("아이디 불일치");

            throw new CustomException(INVALID_TOKEN);
        }


        // 검증 후 재발급용 UserEntity 생성
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        tokenProvider.validateRefreshToken(refreshToken);


        String redisToken = redisService.getData("refresh_token:" + loginId);

        if (redisToken == null || !redisToken.equals(refreshToken)) {

            throw new CustomException(INVALID_TOKEN);
        }





        redisService.deleteData("refresh_token:" + loginId);

        String newAccessToken = tokenProvider.createAccessToken(userEntity.getLoginId(), userEntity.getEmail(), userEntity.getUserType());
        String newRefreshToken = tokenProvider.createRefreshToken(userEntity.getLoginId());

        return new TokenDto(userEntity.getLoginId(), newAccessToken, newRefreshToken);


    }
}
