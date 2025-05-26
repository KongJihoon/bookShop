package com.example.bookshop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserInfo {


    @AllArgsConstructor
    @Getter
    public static class Request {

        private String password;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String loginId;

        private String email;

        private String nickname;

        private LocalDate birth;

        private String phone;

        private String address;

        private String userType;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public static Response fromDto(UserDto userDto) {

            return Response.builder()
                    .loginId(userDto.getLoginId())
                    .email(userDto.getEmail())
                    .nickname(userDto.getNickname())
                    .birth(userDto.getBirth())
                    .phone(userDto.getPhone())
                    .address(userDto.getAddress())
                    .userType(userDto.getUserType())
                    .createdAt(userDto.getCreatedAt())
                    .updatedAt(userDto.getUpdatedAt())
                    .build();

        }

    }

}
