package com.example.bookshop.user.dto;

import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.type.UserState;
import com.example.bookshop.user.type.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;


public class SignUpUserDto {

    @Getter
    @AllArgsConstructor
    @Setter
    @Builder
    public static class Request {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String loginId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()])[a-zA-Z\\d~!@#$%^&*()]{8,13}$",
        message = "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인을 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()])[a-zA-Z\\d~!@#$%^&*()]{8,13}$",
                message = "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
        private String checkPassword;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

        @JsonFormat(shape = JsonFormat.Shape.STRING
                , pattern = "yyyy-MM-dd"
                , timezone = "Asia/Seoul")
        private LocalDate birth;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
        private String phone;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;


        public static UserEntity toEntity(Request request) {

            return UserEntity.builder()
                    .loginId(request.loginId)
                    .password(request.password)
                    .nickname(request.nickname)
                    .email(request.email)
                    .birth(request.birth)
                    .phone(request.phone)
                    .address(request.address)
                    .userType(UserType.USER)
                    .userState(UserState.REQUEST)
                    .build();

        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String loginId;

        private String email;

        private LocalDate birth;

        private String phone;

        private String address;

        private String nickname;

        private String userType;

        private String userState;

        public static Response fromDto(UserDto userDto) {

            return Response.builder()
                    .loginId(userDto.getLoginId())
                    .email(userDto.getEmail())
                    .birth(userDto.getBirth())
                    .phone(userDto.getPhone())
                    .address(userDto.getAddress())
                    .nickname(userDto.getNickname())
                    .userType(String.valueOf(userDto.getUserType()))
                    .userState(String.valueOf(userDto.getUserState()))
                    .build();
        }


    }

}
