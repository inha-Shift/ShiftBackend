package com.inha.shift.jwt;

import com.inha.shift.dto.MemberInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(MemberInfoDto member) {
        return createToken(member, accessTokenExptime);
    }

    /**
     * JWT 생성
     * @param member, expireTime
     * @return JWT String
     */
    private String createToken(MemberInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getMemSq());
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
     * Token에서 User Id 가져오기
     * @param token
     * @return Member Id
     */
    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("memberId", Long.class);
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
}