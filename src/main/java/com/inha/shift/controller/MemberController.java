package com.inha.shift.controller;

import com.inha.shift.domain.Member;
import com.inha.shift.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public void login() {
        System.out.println("login");
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<String> signUp(@RequestBody Member member) {
        Long saveId = memberService.signUp(member);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
