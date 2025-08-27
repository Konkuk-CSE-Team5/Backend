package org.example.backend.domain.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Tag(name = "Test", description = "테스트 API")
@RestController
public class TestController {

    @Operation(
            summary = "테스트 API",
            description = "로그인 사용자 id 및 권한 테스트용 API"
    )
    @CustomExceptionDescription(SwaggerResponseDescription.DEFAULT)
    @GetMapping("/test")
    public BaseResponse<String> test(@LoginUserId Long userId){
        return new BaseResponse<>("userId is " + userId);
    }

    @Operation(
            summary = "테스트 API",
            description = "예외 테스트용 API"
    )
    @CustomExceptionDescription(SwaggerResponseDescription.DEFAULT)
    @GetMapping("/exception")
    public BaseResponse<Void> exception(){
        throw new CustomException(BAD_REQUEST);
    }
}
