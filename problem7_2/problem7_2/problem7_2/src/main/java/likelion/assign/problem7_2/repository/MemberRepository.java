package likelion.assign.problem7_2.repository;

import likelion.assign.problem7_2.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAll();
}
