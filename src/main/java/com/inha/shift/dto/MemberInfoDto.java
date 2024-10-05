package com.inha.shift.dto;

import com.inha.shift.enums.Role;
import lombok.Getter;

@Getter
public class MemberInfoDto {
    private Long memSq;
    private String loginId;
    private Role role;
}
