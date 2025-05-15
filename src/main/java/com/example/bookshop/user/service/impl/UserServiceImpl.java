package com.example.bookshop.user.service.impl;

import com.example.bookshop.global.dto.CheckDto;
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


        validationUserInfo(request);

        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity user = userRepository.save(SignUpUserDto.Request.toEntity(request));




        return ResultDto.of("회원가입에 성공하였습니다.", SignUpUserDto.Response.fromDto(UserDto.fromEntity(user)));
    }

    @Override
    @Transactional
    public CheckDto checkEmail(String email) {

        boolean exists = userRepository.existsByEmail(email);

        if (exists) {
            throw new CustomException(EXISTS_BY_EMAIL);
        }

        return CheckDto.builder()
                .success(true)
                .message("사용가능한 이메일 입니다.").build();

    }

    private void validationUserInfo(SignUpUserDto.Request request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(EXISTS_BY_EMAIL);
        }

        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new CustomException(EXISTS_BY_LOGIN_ID);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(EXISTS_BY_NICKNAME);
        }
    }


}
