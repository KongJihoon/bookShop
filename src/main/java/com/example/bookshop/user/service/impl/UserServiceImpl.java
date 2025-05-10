package com.example.bookshop.user.service.impl;

import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bookshop.global.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResultDto<SignUpUserDto.Response> signUpUser(SignUpUserDto.Request request) {

        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        boolean byEmail = userRepository.existsByEmail(request.getEmail());

        boolean byLoginId = userRepository.existsByLoginId(request.getLoginId());

        boolean byNickname = userRepository.existsByNickname(request.getNickname());

        if (byEmail) {
            throw new CustomException(EXISTS_BY_EMAIL);
        }

        if (byLoginId) {
            throw new CustomException(EXISTS_BY_LOGIN_ID);
        }
        if (byNickname) {
            throw new CustomException(EXISTS_BY_NICKNAME);
        }

        UserEntity user = userRepository.save(SignUpUserDto.Request.toEntity(request));




        return ResultDto.of("회원가입에 성공하였습니다.", SignUpUserDto.Response.fromDto(UserDto.fromEntity(user)));
    }
}
