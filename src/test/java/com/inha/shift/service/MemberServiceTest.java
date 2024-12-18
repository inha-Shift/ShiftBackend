package com.inha.shift.service;

import com.inha.shift.domain.Member;
import com.inha.shift.dto.SignUpDTO;
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
        SignUpDTO signUpDto = new SignUpDTO();
        signUpDto.setEmail("rnjsdndud00");
        signUpDto.setPassword("1234");
        signUpDto.setRole(Role.USER);
        signUpDto.setNickname("말캉도마뱀");
        signUpDto.setStdntNum(1223401);

        //when
        Long saveMemId = memberService.signUp(signUpDto);
        Member findMember = memberService.findMemberById(saveMemId);

        //then
        assertEquals(signUpDto.getEmail(), findMember.getEmail());
    }
}