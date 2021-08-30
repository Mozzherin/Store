package org.example.entityes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@ToString
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name = "photoProfile")
    private String photoProfile;
    @NotEmpty(message = "Логин не должен быть пустым")
    @Size(min=2, max=15, message = "Логин должен содержать от 2-х до 15-ти символов")
    @Column(name = "username")
    private String username;
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min=8, message = "Пароль не должен быть короче 8-ми символов")
    @Column(name = "password")
    private String password;
    @NotEmpty(message = "e-mail не должен быть пустым")
    @Email(message = "Некорректный e-mail")
    @Column(name = "email")
    private String email;
    @Column(name = "activationCode")
    private String activationCode;
    @Column(name = "active")
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
        return active;
    }
}
