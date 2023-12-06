package onlineshop.shop.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role{
    ADMIN(Set.of(Permission.DEVELOPERS_ADMIN,Permission.DEVELOPERS_ORDER,Permission.DEVELOPERS_READ))
    ,USER(Set.of(Permission.DEVELOPERS_ORDER,Permission.DEVELOPERS_READ)) ;

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> getAuthority(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
