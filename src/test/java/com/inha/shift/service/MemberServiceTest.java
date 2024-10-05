package com.inha.shift.service;

import com.inha.shift.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void signUp() throws Exception {
        //given
        Member member = new Member();
        member.setEmail("rnjsdndud00");

        //when
        Long saveMemId = memberService.signUp(member);
        Member findMember = memberService.findMemberById(saveMemId);

        //then
        assertEquals(member.getEmail(), findMember.getEmail());
    }
}