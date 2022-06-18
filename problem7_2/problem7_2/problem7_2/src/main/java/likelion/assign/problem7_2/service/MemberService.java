package likelion.assign.problem7_2.service;

import likelion.assign.problem7_2.domain.Member;
import likelion.assign.problem7_2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Long save(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }

    public Member login(String loginId, String loginPw){
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getLoginPw().equals(loginPw))
                .orElse(null);
    }
}
