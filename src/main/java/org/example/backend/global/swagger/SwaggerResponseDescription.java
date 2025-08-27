package org.example.backend.global.swagger;

import lombok.Getter;
import org.example.backend.global.common.response.status.BaseExceptionResponseStatus;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.*;


@Getter
public enum SwaggerResponseDescription {

    DEFAULT(new LinkedHashSet<>());


    private final Set<BaseExceptionResponseStatus> exceptionResponseStatusSet;

    SwaggerResponseDescription(Set<BaseExceptionResponseStatus> exceptionResponseStatusSet) {
        exceptionResponseStatusSet.addAll(new LinkedHashSet<>(Set.of(
                BAD_REQUEST,
                UNAUTHORIZED,
                FORBIDDEN,
                API_NOT_FOUND,
                METHOD_NOT_ALLOWED,
                INTERNAL_SERVER_ERROR
        )));

        this.exceptionResponseStatusSet = exceptionResponseStatusSet;
    }
}
