package likelion.assign.problem7_2.controller;

import likelion.assign.problem7_2.common.exception.LoginException;
import likelion.assign.problem7_2.common.session.SessionConst;
import likelion.assign.problem7_2.domain.Member;
import likelion.assign.problem7_2.domain.form.LoginForm;
import likelion.assign.problem7_2.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/test")
    void main(){ // 테스트를 위한 관리자 member와 일반 사용자 member2 생성 및 저장
        Member member = new Member();
        member.setLoginPw("1020"); member.setLoginId("user1"); member.setMemberRole(1);

        Member member2 = new Member();
        member2.setLoginPw("0000"); member2.setLoginId("user2"); member2.setMemberRole(2);

        memberService.save(member);
        memberService.save(member2);
    }

    @GetMapping("/usr/member/login")
    ResponseEntity<Object> login(){
        return ResponseEntity.status(HttpStatus.OK).body(new LoginForm());
    }

    @PostMapping("/usr/member/doLogin")
    ResponseEntity<Object> doLogin(@RequestParam("loginId")String email, @RequestParam("loginPw")String password, HttpServletRequest request){
        System.out.println(email+" "+password);
        Member loginMember = memberService.login(email, password);

        if(loginMember == null){
            System.out.println("null 입니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginException("아이디와 비밀번호가 다릅니다."));
        }

        System.out.println(loginMember);
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return ResponseEntity.status(HttpStatus.OK).body(loginMember);
    }
}
