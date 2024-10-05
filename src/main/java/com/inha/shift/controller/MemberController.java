package com.inha.shift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @PostMapping("/api/signUp")
    public ResponseEntity<String> signUp() {
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
