package org.example.backend.global.common.response.status;

public interface ResponseStatus {
    boolean getSuccess();

    int getCode();

    String getMessage();

}