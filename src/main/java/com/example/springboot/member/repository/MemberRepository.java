package com.example.springboot.member.repository;

import com.example.springboot.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    /**
     @Query("select m from Member m " +
            "left join fetch m.todos t " +
            "left join fetch t.likes l")

     이런식의 두개 이상의 엔티티에서 멀티 리스트?bag를 가져오면 Hibernate에서 MultipleBagFetchException 이라는 예외 발생.
     bag는 List나 Set과 같은 컬렉션을 매핑하는 특정한 형태의 매핑 유형.

     해결방법 : distinct, fetch조인 하나만 사용

     inner join은 관련된 엔티티가 반드시 존재해야 하는 조인을 의미하며, todos나 likes가  비어있는 경우 해당 회원을 가져오지 못하게 됩
    */

    // member에서 todo를 조회할때 fetch가 적용되어 N + 1이 발생하지 않지만 todo에서 like를 조회할 경우
    @Query("select distinct m from Member m left join fetch m.todos t")
    Page<Member> findAllMembersWithTodosAndLikes(Pageable pageable);
}
