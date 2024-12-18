package com.inha.shift.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.sercurity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        // signIn으로 들어오는 요청에만 JwtAuthenticationFilter를 호출
        setFilterProcessesUrl("/auth/signIn");
    }

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // HTTP 요청의 JSON 데이터를 LoginRequestDto 객체로 변환
            LoginRequestDto loginRequestDto =
                    new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword(),
                            null // 권한
                    )
            );
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");
        Member member = ((CustomUserDetails) authResult.getPrincipal()).member();
        String token = jwtUtil.createAccessToken(member);
        jwtUtil.addJwtToCookie(token, response);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }

}
