package com.example.bookshop.user.controller;


import com.example.bookshop.global.dto.ResultDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ResultDto<SignUpUserDto.Response>> signUpUser(@RequestBody @Valid SignUpUserDto.Request request) {

        ResultDto<SignUpUserDto.Response> responseResultDto = userService.signUpUser(request);

        return ResponseEntity.ok(responseResultDto);

    }

}
