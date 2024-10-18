package com.inha.shift.service;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.enums.Role;
import com.inha.shift.sercurity.jwt.JwtUtil;
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
    private final ModelMapper modelMapper;

    /**
     * 회원가입
     * @param memberDto
     * @return MemberSq
     */
    @Transactional
    public Long signUp(MemberInfoDto memberDto) {
        // 비밀번호 암호화
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        if(memberRepository.existsByEmail(memberDto.getEmail()) || memberRepository.existsByStdntNum(memberDto.getStdntNum())) {
            throw new BadCredentialsException("Email already in use");
        }
        Member member = modelMapper.map(memberDto, Member.class);
        member.setRole(Role.USER);
        Member saveMember = memberRepository.save(member);
        return saveMember.getMemSq();
    }

    public Member findMemberById(Long id) {
        Optional<Member> findMember = memberRepository.findMemberByMemSq(id);
        if(findMember.isPresent()) {
            return findMember.get();
        } else {
            throw new NoSuchElementException("Member not found");
        }
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }
}
