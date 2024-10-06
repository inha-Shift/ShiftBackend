package com.inha.shift.domain;

import com.inha.shift.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "mem_sq")
    private Long memSq;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "stdnt_num", nullable = false)
    private int stdntNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    private char memDelYn = 'n';

}