package com.example.bookshop.user.controller;


import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;

    private final MailService mailService;

    @PostMapping("/signup")
    public ResponseEntity<ResultDto<SignUpUserDto.Response>> signUpUser(@RequestBody @Valid SignUpUserDto.Request request) {

        ResultDto<SignUpUserDto.Response> responseResultDto = userService.signUpUser(request);

        return ResponseEntity.ok(responseResultDto);

    }

    @PostMapping("/check-email")
    public ResponseEntity<CheckDto> checkEmail(
            @RequestBody SendMailDto sendMailDto

    ) {

        CheckDto checkDto = userService.checkEmail(sendMailDto.getEmail());

        return ResponseEntity.ok(checkDto);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<CheckDto> sendEmailAuth(
            @RequestBody SendMailDto sendMailDto
            ) {

        CheckDto checkDto = mailService.sendAuthMail(sendMailDto.getEmail());

        return ResponseEntity.ok(checkDto);
    }

    @PostMapping("check-auth-code")
    public ResponseEntity<CheckDto> checkAuthCode(@RequestBody CheckEmailDto checkEmailDto) {

        CheckDto checkDto = mailService.checkAuthCode(checkEmailDto.getEmail(), checkEmailDto.getCode());

        return ResponseEntity.ok(checkDto);

    }

    @GetMapping("/userinfo")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserDto> getUserInfo(
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        userService.getUserInfo(userEntity.getUserId());
        return ResponseEntity.ok(UserDto.fromEntity(userEntity));

    }

    @PatchMapping("/edit-user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<UserDto>> editUser(
            @AuthenticationPrincipal UserEntity userEntity,
            @Valid @RequestBody EditUserInfo editUserInfo
    ) {

        ResultDto<UserDto> userDtoResultDto = userService.editUserInfo(userEntity.getUserId(), editUserInfo);

        return ResponseEntity.ok(userDtoResultDto);
    }

}
