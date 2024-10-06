package com.inha.shift.dto;

import com.inha.shift.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberInfoDto {
    private Long memSq;
    private String email;
    private String password;
    private String nickname;
    private int stdntNum;
    private Role role;
}