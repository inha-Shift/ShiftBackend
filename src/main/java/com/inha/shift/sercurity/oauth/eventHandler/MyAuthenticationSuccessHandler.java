package com.inha.shift.sercurity.oauth.eventHandler;

import com.inha.shift.domain.Member;
import com.inha.shift.repository.MemberRepository;
import com.inha.shift.sercurity.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    // OAuth 계정 로그인 성공한 이후 로직
    /**
     * OAuth의 Access Token은 사용하지 않으므로 가져오지 않았음.
     * OAuth의 Access Token은 외부 API 사용하는데 쓰임.
     **/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 인증된 사용자 정보를 가져온다
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");

        Optional<Member> optionalMember = memberRepository.findMemberByEmail(email);

        // 멤버 권한 확인 후 권한 재설정
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String role = "ROLE_" + member.getRole();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            oAuth2User, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
                    )
            );

            // JWT 토큰 발행
            String oAuthToken = jwtUtil.createAccessToken(member);
            jwtUtil.addJwtToCookie(oAuthToken, response);

            // 메인 페이지로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, "/");
        } else {
            // 회원이 존재하지 않을 경우
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/sign")
                    .queryParam("email", (String) oAuth2User.getAttribute("email"))
                    .queryParam("provider", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
