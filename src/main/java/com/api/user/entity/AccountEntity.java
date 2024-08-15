package com.api.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class AccountEntity implements UserDetails {

    @Id
    @Column(name="login_id")
    private String loginId;

    @Column(name="password")
    private String password;

    @Column(name="creator")
    private String creator;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updater")
    private String updater;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;  // 암호화된 비밀번호 반환
    }
    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Builder
    public AccountEntity(String loginId, String password){
        this.loginId = loginId;
        this.password = password;
    }
}
