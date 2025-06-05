package com.example.bookshop.user.controller;


import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.global.service.MailService;
import com.example.bookshop.user.dto.*;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 API 컨트롤러
 * 회원가입, 이메일 인증, 회원정보 조회 및 수정, 탈퇴 기능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;

    private final MailService mailService;

    /**
     * 유저 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ResultDto<SignUpUserDto.Response>> signUpUser(@RequestBody @Valid SignUpUserDto.Request request) {

        ResultDto<SignUpUserDto.Response> responseResultDto = userService.signUpUser(request);

        return ResponseEntity.ok(responseResultDto);

    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/check-email")
    public ResponseEntity<CheckDto> checkEmail(
            @RequestBody SendMailDto sendMailDto

    ) {

        CheckDto checkDto = userService.checkEmail(sendMailDto.getEmail());

        return ResponseEntity.ok(checkDto);
    }

    /**
     * 이메일 인증코드 전송
     */
    @PostMapping("/send-mail")
    public ResponseEntity<CheckDto> sendEmailAuth(
            @RequestBody SendMailDto sendMailDto
            ) {

        CheckDto checkDto = mailService.sendAuthMail(sendMailDto.getEmail());

        return ResponseEntity.ok(checkDto);
    }

    /**
     * 이메일 인증코드 검증 API
     */
    public ResponseEntity<CheckDto> checkAuthCode(@RequestBody CheckEmailDto checkEmailDto) {

        CheckDto checkDto = mailService.checkAuthCode(checkEmailDto.getEmail(), checkEmailDto.getCode());

        return ResponseEntity.ok(checkDto);

    }


    /**
     * 유저 정보 조회
     */
    @GetMapping("/userinfo")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDto> getUserInfo(
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        userService.getUserInfo(userEntity.getUserId());
        return ResponseEntity.ok(UserDto.fromEntity(userEntity));

    }

    /**
     * 유저 정보 수정
     */
    @PatchMapping("/edit-user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<UserDto>> editUser(
            @AuthenticationPrincipal UserEntity userEntity,
            @Valid @RequestBody EditUserInfo editUserInfo
    ) {

        ResultDto<UserDto> userDtoResultDto = userService.editUserInfo(userEntity.getUserId(), editUserInfo);

        return ResponseEntity.ok(userDtoResultDto);
    }


    /**
     * 유저 탈퇴 API
     */
    @PatchMapping("/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CheckDto> deleteUser(
            @RequestBody @Valid DeleteUserDto deleteUserDto,
            @AuthenticationPrincipal UserEntity userEntity
    ) {


        return ResponseEntity.ok(userService.deleteUser(userEntity.getLoginId(), deleteUserDto.getPassword()));
    }

}
