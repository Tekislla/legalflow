package br.com.legalflow;

public enum RoleEnum {
    ADMIN,
    USER;

    public static RoleEnum getRole(boolean isAdmin) {
        return isAdmin ? ADMIN : USER;
    }
}
