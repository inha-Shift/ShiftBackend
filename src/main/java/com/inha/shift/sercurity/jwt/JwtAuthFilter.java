package com.inha.shift.jwt;

import com.inha.shift.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter: 한 번 실행 보장

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    // 각 HTTP 요청마다 실행, JWT 토큰 검증 and 사용자 인증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // HTTP 요청에 들어있는 Authorization은 Bearer로 시작한다.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // HTTP 요청에 들어있는 토큰 가져오기
            String token = authHeader.substring(7);

            // 유효성 검증 (Signature 확인, Expire Time 확인)
            if (jwtUtil.validateToken(token)) {
                Long memberSq = jwtUtil.getMemberSqFromToken(token);

                // 현재 사용자 객체 생성
                UserDetails userDetails = customUserDetailService.loadUserByUsername(memberSq.toString());

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
        // 다음 필터, 서블릿으로 요청 전달
        filterChain.doFilter(request, response);
    }
}
