package org.example.backend.global.common.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus{
    // 성공
    SUCCESS(HttpStatus.OK.value(), 200, "요청에 성공했습니다."),

    // 공통 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),400, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 401, "인증 자격이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), 403, "권한이 없습니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND.value(),404, "존재하지 않는 API입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), 405, "유효하지 않은 Http 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버 내부 오류입니다."),

    // JWT 토큰
    INVALID_JWT(HttpStatus.UNAUTHORIZED.value(), 401, "올바르지 않은 토큰입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED.value(), 401, "만료된 토큰입니다"),
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), 400, "토큰을 찾을 수 없습니다");


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
