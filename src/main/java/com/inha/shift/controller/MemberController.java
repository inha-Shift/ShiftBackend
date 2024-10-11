package com.inha.shift.controller;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

//    @PostMapping("/auth/signIn")
//    public void login(@RequestBody LoginRequestDto loginMemberDto, HttpServletResponse response) {
//        String jwtToken = memberService.signIn(loginMemberDto);
//        System.out.println("jwtToken: " + jwtToken);
//        ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .sameSite("Strict") // Strict, Lax, None
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<String> signUp(@RequestBody MemberInfoDto memberDto) {
        Long saveId = memberService.signUp(memberDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
