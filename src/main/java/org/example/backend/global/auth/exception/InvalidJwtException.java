package org.example.backend.global.auth.exception;


import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.common.response.status.ResponseStatus;

public class InvalidJwtException extends CustomException {
    public InvalidJwtException(ResponseStatus exceptionStatus){
        super(exceptionStatus);
    }
}
