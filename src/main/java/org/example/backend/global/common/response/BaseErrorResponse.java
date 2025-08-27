package org.example.backend.global.common.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.backend.global.common.response.status.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"status", "code", "message", "timestamp"})
public class BaseErrorResponse implements ResponseStatus {
    @Schema(example = "200")
    private final int status;

    @Schema(example = "200")
    private final int code;

    @Schema(example = "요청에 성공하였습니다.")
    private final String message;

    @Schema(example = "2025-01-01 00:00:00")
    private final LocalDateTime timestamp;

    @JsonCreator
    public BaseErrorResponse(
            @JsonProperty("status") int status,
            @JsonProperty("code") int code,
            @JsonProperty("message") String message,
            @JsonProperty("timestamp") LocalDateTime timestamp
    ) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public BaseErrorResponse(ResponseStatus status) {
        this.status = status.getStatus();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public BaseErrorResponse(ResponseStatus status, String message) {
        this.status = status.getStatus();
        this.code = status.getCode();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

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
