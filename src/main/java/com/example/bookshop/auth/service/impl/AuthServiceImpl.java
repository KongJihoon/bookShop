package com.example.bookshop.auth.service.impl;

import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.auth.service.AuthService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.global.security.TokenProvider;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bookshop.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;


    /**
     * 유저 로그인
     */
    @Override
    @Transactional
    public UserDto LoginUser(String loginId, String password) {


        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }

        if (!user.isEmailAuth()) {
            throw new CustomException(PROCEED_EMAIL_AUTH);
        }


        return UserDto.fromEntity(user);
    }


    /**
     * JWT 토큰 발급
     */
    @Override
    public TokenDto getToken(UserDto userDto) {

        String accessToken = tokenProvider.createAccessToken(userDto.getLoginId(), userDto.getEmail(), UserType.valueOf(userDto.getUserType()));

        String refreshToken = tokenProvider.createRefreshToken(userDto.getLoginId());


        return new TokenDto(userDto.getLoginId(), accessToken, refreshToken);
    }
}
