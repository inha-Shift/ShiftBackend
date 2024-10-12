package com.inha.shift.repository;

import com.inha.shift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByStdntNum(int stdntNum);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByMemSq(Long memberSq);
}
