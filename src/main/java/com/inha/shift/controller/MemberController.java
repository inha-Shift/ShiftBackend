package com.inha.shift.controller;

import com.inha.shift.dto.SignUpDTO;
import com.inha.shift.sercurity.jwt.JwtUtil;
import com.inha.shift.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO memberDto) {
        Long saveId = memberService.signUp(memberDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

//    @GetMapping("/confirmAuth")
//    public ResponseEntity<String> confirmAuth() {
//        // SecurityContextHolder에서 인증 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        if (jwtUtil.validateToken(username)) {
//            System.out.println("인증됨");
//            return new ResponseEntity<>("Authenticated user: " + username, HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Invalid JWT Token", HttpStatus.UNAUTHORIZED);
//    }
}
