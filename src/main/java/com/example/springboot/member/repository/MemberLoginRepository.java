package com.example.springboot.member.repository;

import com.example.springboot.member.domain.entity.MemberLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberLoginRepository extends JpaRepository<MemberLogin, Long> {

    @Query("select ml from MemberLogin ml where ml.member.id = :memberId")
    Optional<MemberLogin> findByMemberId(@Param("memberId") Long memberId);
}
