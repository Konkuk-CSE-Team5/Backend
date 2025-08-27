package org.example.backend.global.auth.constant;

public enum JwtAutenticationFilterConstants {
    AUTHORIZATION("Authorization"),
    TOKEN_PREFIX("Bearer "),
    TOKEN_FOR_ARGUMENT_RESOLVER("token"),
    JWT_ERROR_CODE("jwt_error_code");

    private final String value;

    private JwtAutenticationFilterConstants(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
