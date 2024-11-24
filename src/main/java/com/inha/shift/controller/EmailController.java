package com.inha.shift.controller;

import com.inha.shift.domain.EmailMessage;
import com.inha.shift.dto.EmailResponseDto;
import com.inha.shift.dto.EmailPostDto;
import com.inha.shift.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private String confirmCode;

    @PostMapping("/auth/sendEmailConfirmNum")
    public ResponseEntity<String> sendEmail(@RequestBody EmailPostDto emailPostDto) {
        EmailMessage message = EmailMessage.builder()
                .to(emailPostDto.getEmail())
                .subject("Shift의 인증번호 발송")
                .message("verificationCodeEmail.html")
                .build();
        String code = emailService.createCode();
        confirmCode = code;
        System.out.println("전송코드: " + code);
        emailService.sendEmail(message, code);
        return new ResponseEntity<>("Email transmission", HttpStatus.OK);
    }

    @PostMapping("/auth/confirmEmail")
    public ResponseEntity<String> confirmEmail(@RequestBody EmailResponseDto emailResponseDto) {
        System.out.println("emailResponseDto: " + emailResponseDto.getConfirmEmailNum());
        if(confirmCode.equals(emailResponseDto.getConfirmEmailNum())) {
            System.out.println("인증코드: " + emailResponseDto.getConfirmEmailNum());
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Email transmission", HttpStatus.BAD_REQUEST);
        }
    }

}
