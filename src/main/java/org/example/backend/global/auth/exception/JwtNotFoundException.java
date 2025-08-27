package org.example.backend.global.auth.exception;

import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.common.response.status.ResponseStatus;

public class JwtNotFoundException extends CustomException {
    public JwtNotFoundException(ResponseStatus exceptionStatus){
        super(exceptionStatus);
    }
}
