package org.example.backend.domain.users.model;

import lombok.Getter;

@Getter
public enum Role {

    VOL("자원봉사자"), ORG("기관");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
