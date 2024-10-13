//package com.inha.shift.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class HomeController {
//    @RequestMapping("/")
//    public ResponseEntity<String> home() {
//        // SecurityContextHolder에서 인증 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String username = authentication.getName();
//            return new ResponseEntity<>("Authenticated user: " + username, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
//        }
//    }
//}
