package com.inha.shift.sercurity;

import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.eventHandler.MyAuthenticationFailureHandler;
import com.inha.shift.eventHandler.MyAuthenticationSuccessHandler;
import com.inha.shift.sercurity.jwt.JwtAuthenticationFilter;
import com.inha.shift.sercurity.jwt.JwtAuthorizationFilter;
import com.inha.shift.sercurity.jwt.JwtUtil;
import com.inha.shift.service.CustomOAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final CustomOAuth2Service oAuth2UserService;
    private final MyAuthenticationSuccessHandler successHandler;
    private final MyAuthenticationFailureHandler failureHandler;
    private final JwtUtil jwtUtil;



    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/signIn", "/auth/signUp", "/auth/sendEmailConfirmNum", "/auth/confirmEmail").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/confirmAuth").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(c -> c.userService(oAuth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                )
                .exceptionHandling(exception ->
                        exception
                                // 허용되지 않은 요청이면 401 리턴
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Required");
                                }))
                .logout(LogoutConfigurer::permitAll)
                // 세션 생성 or 사용 X
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtAuthorizationFilter(customUserDetailService, jwtUtil), JwtAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(
                        jwtUtil, authenticationConfiguration.getAuthenticationManager()),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // 허용할 origin 추가
        config.addAllowedOrigin("http://localhost:8080"); // 허용할 origin 추가
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}