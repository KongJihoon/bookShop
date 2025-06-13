package com.example.bookshop.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),


    // JWT
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 갱신해주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),


    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    INVALID_PATTERN(HttpStatus.BAD_REQUEST, "잘못된 패턴입니다."),
    PAST_BIRTHDAY(HttpStatus.BAD_REQUEST, "과거 날짜를 입력해주세요."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    EXISTS_BY_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    EXISTS_BY_LOGIN_ID(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    EXISTS_BY_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다."),
    WITHDRAW_USER(HttpStatus.BAD_REQUEST, "회원 탈퇴한 유저입니다."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증번호입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    ALREADY_EMAIL_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증이 완료된 이메일입니다."),
    PROCEED_EMAIL_AUTH(HttpStatus.BAD_REQUEST, "이메일 인증을 진행해주세요."),
    ALREADY_DELETE_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다."),



    // book
    ALREADY_CREATE_BOOK(HttpStatus.BAD_REQUEST, "이미 등록된 책입니다."),
    NOT_FOUND_BOOK(HttpStatus.BAD_REQUEST, "도서를 찾을 수 없습니다."),
    NOT_PERMISSION_BOOK(HttpStatus.BAD_REQUEST, "도서에 대한 권한이 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "이미지 삭제에 실패하였습니다."),


    // category
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."),
    ALREADY_EXIST_CATEGORY(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");


    private final HttpStatus status;
    private final String message;


    public int statusCode() {
        return this.status.value();
    }
}
