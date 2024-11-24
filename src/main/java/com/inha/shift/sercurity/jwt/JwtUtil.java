package com.inha.shift.sercurity.jwt;

import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExptime;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration_time}") long accessTokenExptime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExptime = accessTokenExptime;
    }

    /**
     * Access Token 생성
     * @param memberDto
     * @return Access Token String
     */
    public String createAccessToken(MemberInfoDto memberDto) {
        return createToken(memberDto, accessTokenExptime);
    }

    public String createOAuthToken(String email, String role) {
        MemberInfoDto memberDto = new MemberInfoDto();
        memberDto.setRole(Role.convertStringToRole(role));
        return createToken(memberDto, accessTokenExptime);
    }

    /**
     * JWT 생성
     * @param member, expireTime
     * @return JWT String
     */
    private String createToken(MemberInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberSq", member.getMemSq());
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenVaildty = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                // 토큰 발행시간
                .setIssuedAt(Date.from(now.toInstant()))
                // 토큰 만료시간
                .setExpiration(Date.from(tokenVaildty.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                // JWT 문자열 생성
                .compact();
    }

    /**
     * Token에서 MemberSq 가져오기
     * @param token
     * @return MemberSq
     */
    public Long getMemberSqFromToken(String token) {
        return parseClaims(token).get("memberSq", Long.class);
    }

    /**
     * Token에서 Email 가져오기
     * @param token
     * @return
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).get("email", String.class);
    }


    /**
     * JWT 검증
     * @param token
     * @return IsValiddatae
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public void addJwtToCookie(String token, HttpServletResponse response) {
        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("jwt", token) // 쿠키 이름과 값 설정
                .httpOnly(true) // 클라이언트에서 접근 불가
                .secure(false) // HTTPS에서만 쿠키 전송, 개발 단계에서는 비활성화
                .path("/") // 모든 경로에서 쿠키 접근 가능
                .sameSite("Strict") // 같은 사이트에서만 전송
                .maxAge(accessTokenExptime) // 쿠키 만료 시간
                .build();
        // 쿠키에 Token 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}