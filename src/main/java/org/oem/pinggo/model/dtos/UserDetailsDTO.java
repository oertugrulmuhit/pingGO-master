package org.oem.pinggo.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Getter
@Slf4j
public class UserDetailsDTO implements UserDetails {

    private final Long id;

    private final String username;

    private final String email;

    @JsonIgnore
    private final String password;

    private final boolean enabled;

    public UserDetailsDTO(Long id, String username, String email, String password, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public static UserDetailsDTO build(User user) {
        UserDetailsDTO userDetails = new UserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(), user.isEnabled());
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsDTO user = (UserDetailsDTO) o;
        return Objects.equals(id, user.id);
    }
}