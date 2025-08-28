package org.example.backend.global.common.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus{
    // 1000 : 성공
    SUCCESS(HttpStatus.OK.value(), 1000, "요청에 성공했습니다."),

    // 2000 : 공통 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),2000, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 2001, "인증 자격이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), 2002, "권한이 없습니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND.value(),2003, "존재하지 않는 API입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), 2004, "유효하지 않은 Http 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 2005, "서버 내부 오류입니다."),

    // 2100 : JWT 토큰 에러
    INVALID_JWT(HttpStatus.UNAUTHORIZED.value(), 2100, "올바르지 않은 토큰입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED.value(), 2101, "만료된 토큰입니다"),
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), 2102, "토큰을 찾을 수 없습니다"),

    // 3000: Auth 관련 에러
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), 3000, "로그인에 실패했습니다"),

    DUPLICATE_USERNAME(HttpStatus.CONFLICT.value(), 3001, "이미 존재하는 아이디로 가입할 수 없습니다" );


    private final int status;
    private final int code;
    private final String message;


    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
