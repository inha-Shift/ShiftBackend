package com.inha.shift.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role convertStringToRole(String roleString) {
        return Role.valueOf(roleString.toUpperCase());
    }
}