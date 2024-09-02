package br.com.legalflow.enums;

public enum RoleEnum {
    ADMIN,
    USER;

    public static RoleEnum getRole(boolean isAdmin) {
        return isAdmin ? ADMIN : USER;
    }
}
