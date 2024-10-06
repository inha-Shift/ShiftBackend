package com.inha.shift.controller;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginMemberDto) {
        String token = memberService.signIn(loginMemberDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<String> signUp(@RequestBody MemberInfoDto memberInfoDto) {
        Long saveId = memberService.signUp(memberInfoDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
