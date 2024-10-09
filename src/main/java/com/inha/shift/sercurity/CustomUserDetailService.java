package com.inha.shift.service;

import com.inha.shift.sercurity.CustomUserDetails;
import com.inha.shift.domain.Member;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // JWT 토큰에서 추출한 PK와 일치하는 데이터가 존재하면 Auth 객체 생성 시 필요한 UserDetail 객체로 반환한다.
    @Override
    public UserDetails loadUserByUsername(String memberSq) throws UsernameNotFoundException {
        System.out.println("멤버" + memberSq);

        Member member = memberRepository.findMemberByMemSq(Long.parseLong(memberSq))
                .orElseThrow(() -> new UsernameNotFoundException(memberSq + "에 해당하는 회원이 없습니다."));
        System.out.println("로그인 성공");
        MemberInfoDto dto = modelMapper.map(member, MemberInfoDto.class);
        return new CustomUserDetails(dto);
    }
}
