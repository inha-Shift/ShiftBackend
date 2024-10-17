package com.inha.shift.service;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.LoginRequestDto;
import com.inha.shift.dto.MemberInfoDto;
import com.inha.shift.enums.Role;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void signUp() throws Exception {
        //given
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setEmail("rnjsdndud00");
        memberInfoDto.setPassword("1234");
        memberInfoDto.setRole(Role.USER);
        memberInfoDto.setNickname("말캉도마뱀");
        memberInfoDto.setStdntNum(1223401);

        //when
        Long saveMemId = memberService.signUp(memberInfoDto);
        Member findMember = memberService.findMemberById(saveMemId);

        //then
        assertEquals(memberInfoDto.getEmail(), findMember.getEmail());
    }

    @Test
    public void signIn() throws Exception {
        // given
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setEmail("rnjsdndud00");
        memberInfoDto.setPassword("1234");
        memberInfoDto.setRole(Role.USER);
        memberInfoDto.setNickname("말캉도마뱀");
        memberInfoDto.setStdntNum(1223401);
        Long saveMemId = memberService.signUp(memberInfoDto);

        // when
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("rnjsdndud00");
        loginRequestDto.setPassword("1234");
        String token = memberService.signIn(loginRequestDto);

        // then
        System.out.println("token: " + token);
    }
}