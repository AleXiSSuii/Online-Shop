package onlineshop.shop.model.enums;

public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_ORDER("developers:order"),
    DEVELOPERS_ADMIN("developers:admin");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
