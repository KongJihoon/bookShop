package com.example.bookshop.auth.controller;


import com.example.bookshop.auth.dto.LoginDto;
import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.auth.service.AuthService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<ResultDto<LoginDto.Response>> loginUser(@RequestBody @Valid LoginDto.Request request) {

        UserDto userDto = authService.LoginUser(request.getLoginId(), request.getPassword());

        TokenDto token = authService.getToken(userDto);


        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", token.getAccessToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(ResultDto.of("로그인에 성공하였습니다.", LoginDto.Response.fromDto(userDto, token.getRefreshToken())));

    }

}
