package org.example.backend.global.auth.util;

public enum JwtClaimKey {
    USER_ID("userId"),
    ROLE("role");
    private String key;

    private JwtClaimKey(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
