package likelion.assign.problem7_2.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Article {

    private Long id;
    private Long memberId; //작성자 id
    private String written; // 작성 시간
    private String lastModified; //수정시간
    private Long views; // 조회수
    private Long recommended; //추천수
    private String title; //제목
    private String body; // 내용
    private int boardId; // 1일때 공지사항 2일 때 자유게시판

}
