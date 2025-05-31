package com.example.bookshop.user.service.impl;

import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.user.dto.EditUserInfo;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import com.example.bookshop.user.type.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.bookshop.global.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public UserDto getUserInfo(Long userId) {

        log.info("유저 정보 조회: loginId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        return UserDto.fromEntity(userEntity);
    }

    @Override
    public ResultDto<UserDto> editUserInfo(Long userId, EditUserInfo editUserInfo) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        userEntity.updateUserInfo(editUserInfo);

        UserDto userDto = UserDto.fromEntity(userEntity);

        return ResultDto.of("사용자 정보를 변경하였습니다.", userDto);
    }

    @Override
    public CheckDto deleteUser(String loginId, String password) {

        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }

        userEntity.setUserState(UserState.WITHDRAW);
        userEntity.setDeletedAt(LocalDateTime.now());

        userRepository.save(userEntity);

        return new CheckDto(true, "회원 탈퇴에 성공하였습니다.");
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


    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
