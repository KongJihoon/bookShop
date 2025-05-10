package com.example.bookshop.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    EXISTS_BY_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    EXISTS_BY_LOGIN_ID(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    EXISTS_BY_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");


    private final HttpStatus status;
    private final String message;


    public int statusCode() {
        return this.status.value();
    }
}
