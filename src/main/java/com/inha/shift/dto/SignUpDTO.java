package com.inha.shift.dto;

import com.inha.shift.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpDTO {
    private String email;
    private String password;
    private String nickname;
    private int stdntNum;
    private Role role;
}