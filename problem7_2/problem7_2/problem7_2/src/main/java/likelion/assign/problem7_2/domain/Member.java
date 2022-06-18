package likelion.assign.problem7_2.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class Member {

    private Long id;
    private String loginId;
    private String loginPw;
    private int memberRole; // 1일때 member 2일 때 관리자
}