package com.example.bookshop.auth.dto;

import com.example.bookshop.user.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class LoginDto {


    @Getter
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String loginId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()])[a-zA-Z\\d~!@#$%^&*()]{8,13}$",
                message = "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
        private String password;

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String loginId;

        private String email;

        private String birth;

        private String phone;

        private String address;

        private String nickname;

        private String userType;

        private String refreshToken;

        public static Response fromDto(UserDto userDto, String refreshToken) {

            return Response.builder()
                    .loginId(userDto.getLoginId())
                    .email(userDto.getEmail())
                    .birth(userDto.getUserType())
                    .phone(userDto.getPhone())
                    .address(userDto.getAddress())
                    .nickname(userDto.getNickname())
                    .userType(userDto.getUserType())
                    .refreshToken(refreshToken)
                    .build();

        }

    }

}
