package org.example.backend.global.common.exception;

import lombok.Getter;
import org.example.backend.global.common.response.status.ResponseStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public CustomException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
