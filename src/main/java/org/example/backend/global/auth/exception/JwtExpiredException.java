package org.example.backend.global.auth.exception;


import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.common.response.status.ResponseStatus;

public class JwtExpiredException extends CustomException {
    public JwtExpiredException(ResponseStatus exceptionStatus){
        super(exceptionStatus);
    }
}
