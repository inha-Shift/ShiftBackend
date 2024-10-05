package com.inha.shift.service;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.jwt.JwtUtil;
import com.inha.shift.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    /**
     * 회원가입
     * @param member
     * @return Member Id
     */
    @Transactional
    public Long signUp(Member member) {
        if(memberRepository.existsByEmail(member.getEmail()) || memberRepository.existsByStdntNum(member.getStdntNum())) {
            return 0L;
        }
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        Member saveMember = memberRepository.save(member);
        return saveMember.getMemSq();
    }

    /**
     * 로그인
     * @param dto
     * @return Access Token
     */
    @Transactional
    public String signIn(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Member member = memberRepository.findMemberByEmail(email);
        if(member == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        MemberInfoDto info = modelMapper.map(member, MemberInfoDto.class);

        return jwtUtil.createAccessToken(info);
    }

    public Member findMemberById(Long id) {
        Optional<Member> findMember = memberRepository.findMemberByMemSq(id);
        if(findMember.isPresent()) {
            return findMember.get();
        } else {
            throw new NoSuchElementException("Member not found");
        }
    }
}
