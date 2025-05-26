package com.example.bookshop.auth.controller;


import com.example.bookshop.auth.dto.LoginDto;
import com.example.bookshop.auth.dto.TokenDto;
import com.example.bookshop.auth.service.AuthService;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.global.security.TokenProvider;
import com.example.bookshop.user.dto.UserDto;
import com.example.bookshop.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final TokenProvider tokenProvider;




    @PostMapping("/login")
    public ResponseEntity<ResultDto<LoginDto.Response>> loginUser(@RequestBody @Valid LoginDto.Request request) {

        UserDto userDto = authService.LoginUser(request.getLoginId(), request.getPassword());

        TokenDto token = authService.getToken(userDto);


        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", token.getAccessToken());
        headers.set("Refresh-Token", token.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(ResultDto.of("로그인에 성공하였습니다.", LoginDto.Response.fromDto(userDto)));

    }


    @PostMapping("/reissue")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CheckDto> reIssueToken(
            HttpServletRequest request,
            @AuthenticationPrincipal UserEntity userEntity
            ) {

        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Refresh-Token");

        if (accessToken == null || refreshToken == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_TOKEN);
        }

        TokenDto tokenDto = authService.reIssueToken(userEntity.getLoginId(), accessToken, refreshToken);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(CheckDto.builder()
                        .success(true)
                        .message("토큰을 갱신하였습니다.").build());
    }

}
