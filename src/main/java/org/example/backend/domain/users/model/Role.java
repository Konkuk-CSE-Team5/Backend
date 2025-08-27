package org.example.backend.domain.users.model;

import lombok.Getter;

@Getter
public enum Role {

    VOL("자원봉사자"), ORG("기관");

    private static final String ROLE_PREFIX = "ROLE_";

    private final String value;

    Role(String value) {
        this.value = value;
    }

    /** Spring Security 권한 식별자 (hasRole/hasAuthority에서 사용하는 값) */
    public String getAuthority() {
        return ROLE_PREFIX + name();
    }
}
