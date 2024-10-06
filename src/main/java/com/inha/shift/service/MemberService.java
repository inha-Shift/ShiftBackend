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
     * @param memberDto
     * @return Member Id
     */
    @Transactional
    public Long signUp(MemberInfoDto memberDto) {
        // 비밀번호 암호화
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        if(memberRepository.existsByEmail(memberDto.getEmail()) || memberRepository.existsByStdntNum(memberDto.getStdntNum())) {
            return null;
        }
        Member member = modelMapper.map(memberDto, Member.class);
        Member saveMember = memberRepository.save(member);
        return saveMember.getMemSq();
    }

    /**
     * 로그인
     * @param loginRequestDto
     * @return Access Token
     */
    @Transactional
    public String signIn(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        Member member = memberRepository.findMemberByEmail(email);
        if(member == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        MemberInfoDto infoDto = modelMapper.map(member, MemberInfoDto.class);
        return jwtUtil.createAccessToken(infoDto);
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
