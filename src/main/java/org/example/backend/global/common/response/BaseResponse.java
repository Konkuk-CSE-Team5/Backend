package org.example.backend.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.backend.global.common.response.status.ResponseStatus;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.SUCCESS;

@Getter
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> implements ResponseStatus {
    private final boolean success;

    @Schema(example = "200")
    private final int code;

    @Schema(example = "요청에 성공하였습니다.")
    private final String message;
    private final T data;

    public BaseResponse(T data) {
        this.success = true;
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.data = data;
    }

    @Override
    public boolean getSuccess() {
        return this.success;
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
