package com.example.bookshop.user.controller;


import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.service.MailService;
import com.example.bookshop.user.dto.CheckEmailDto;
import com.example.bookshop.user.dto.SendMailDto;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
