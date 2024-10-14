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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody MemberInfoDto memberDto) {
        Long saveId = memberService.signUp(memberDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping("/confirmAuth")
    public ResponseEntity<String> confirmAuth() {
        // SecurityContextHolder에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("인증됨");
            String username = authentication.getName();
            return new ResponseEntity<>("Authenticated user: " + username, HttpStatus.OK);
        } else {
            System.out.println("confuse");
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
    }
}
