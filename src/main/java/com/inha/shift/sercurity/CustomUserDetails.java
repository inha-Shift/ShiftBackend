package com.inha.shift.domain;

import com.inha.shift.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final MemberInfoDto member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Security는 기본적으로 여러 개의 권한을 할당할 수 있는 유연성을 제공함
        List<String> roles = new ArrayList<>();
        // Security에서 권한을 검증할 때 ROLE_ADMIN 같은 방식으로 검증함
        roles.add("ROLE_" + member.getRole().toString());
        // Security에서 사용할 수 있도록 GrantedAuthority 형식으로 변환해서 저장
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
}