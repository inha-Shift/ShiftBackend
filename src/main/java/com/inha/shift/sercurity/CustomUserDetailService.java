package com.inha.shift.sercurity;

import com.inha.shift.domain.Member;
import com.inha.shift.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // JWT 토큰에서 추출한 PK와 일치하는 데이터가 존재하면 Auth 객체 생성 시 필요한 UserDetail 객체로 반환한다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return new CustomUserDetails(member);
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
