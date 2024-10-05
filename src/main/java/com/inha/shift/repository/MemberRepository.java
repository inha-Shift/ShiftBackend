package com.inha.shift.repository;

import com.inha.shift.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByStdntNum(int stdntNum);
    Member findMemberByLoginId(String loginId);
    Optional<Member> findMemberByMemSq(Long memberSq);
}
