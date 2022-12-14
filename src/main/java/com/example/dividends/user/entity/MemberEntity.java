package com.example.dividends.user.entity;


import com.example.dividends.model.constants.Authority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="MEMBER")
@Builder
@ToString


// UserDetails 구현 => Spring security 의 인증 작업 절차를 받음
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    private String roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<String> roles = new ArrayList<>();
        roles.add(this.roles);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    }


    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
