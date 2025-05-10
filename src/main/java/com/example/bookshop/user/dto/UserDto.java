package com.example.bookshop.user.dto;

import com.example.bookshop.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long userId;

    private String loginId;

    private String password;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    private String nickname;

    private String userType;

    public static UserDto fromEntity(UserEntity userEntity) {

        return UserDto.builder()
                .userId(userEntity.getUserId())
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .birth(userEntity.getBirth())
                .phone(userEntity.getPhone())
                .address(userEntity.getAddress())
                .nickname(userEntity.getNickname())
                .userType(String.valueOf(userEntity.getUserType()))
                .build();
    }

}
