package com.inha.shift.sercurity.jwt;

import com.inha.shift.sercurity.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter { // OncePerRequestFilter: 한 번 실행 보장

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(CustomUserDetailService customUserDetailService, JwtUtil jwtUtil) {
        this.customUserDetailService = customUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    // 각 HTTP 요청마다 실행, JWT 토큰 검증 and 사용자 인증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 쿠키 가져오기
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("jwt")) {
                    // 쿠키에서 토큰 가져오기
                    String token = cookie.getValue();
                    // 유효성 검증 (Signature 확인, Expire Time 확인)
                    if(token != null && jwtUtil.validateToken(token)) {
                        String email = jwtUtil.getEmailFromToken(token);
                        // 현재 사용자 객체 생성
                        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
                        // 사용자 인증 정보 생성
                        if(userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication =
                                    // 사용자 객체, 자격증명(이미 로그인 된 상태이므로 null), 사용자 권한 정보
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            // Security Context에 현재 인증된 사용자의 인증 정보 설정
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }
        // 다음 필터, 서블릿으로 요청 전달
        filterChain.doFilter(request, response);
    }
}
