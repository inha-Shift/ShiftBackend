package com.inha.shift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
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
