package likelion.assign.problem7_2.controller;

import likelion.assign.problem7_2.common.exception.ArticleFindException;
import likelion.assign.problem7_2.common.exception.ArticleUpdateException;
import likelion.assign.problem7_2.common.session.SessionConst;
import likelion.assign.problem7_2.domain.Article;
import likelion.assign.problem7_2.domain.Member;
import likelion.assign.problem7_2.domain.form.ArticleModifyForm;
import likelion.assign.problem7_2.domain.form.ArticleSaveForm;
import likelion.assign.problem7_2.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/board/test")
    String test(){
        for(int i=0;i<300;i++){
            Article article=new Article();
            article.setBoardId(1);
            article.setMemberId(1L);
            article.setBody("Content"+i);
            article.setViews(0L);
            article.setWritten(LocalDateTime.now().toString());
            article.setTitle("title"+i);
            article.setRecommended(0L);
            articleService.save(article);
        }
        for(int i=0;i<300;i++){
            Article article=new Article();
            article.setBoardId(2);
            article.setMemberId(2L);
            article.setBody("Content"+i);
            article.setViews(0L);
            article.setWritten(LocalDateTime.now().toString());
            article.setTitle("title"+i);
            article.setRecommended(0L);
            articleService.save(article);
        }
        return "ok";
    }

    @GetMapping("/usr/home/main")
    ResponseEntity<Object> home(){
        return ResponseEntity.status(HttpStatus.OK).body(articleService.findAllRecent());
    }

    @GetMapping("/usr/article/list")
    ResponseEntity<Object> board(@RequestParam HashMap<String,String> paramMap){
        int boardId = Integer.parseInt(paramMap.get("boardId"));
        if(paramMap.size()==1){
            return ResponseEntity.status(HttpStatus.OK).body(articleService.findAll(boardId));
        }
        else{
            if(paramMap.containsKey("page")){
                int pageNum = Integer.parseInt(paramMap.get("page"));
                System.out.println(pageNum);
                return ResponseEntity.status(HttpStatus.OK).body(articleService.findAll(boardId,pageNum));
            }
            else{
                String keyword = paramMap.get("searchKeyword");
                return ResponseEntity.status(HttpStatus.OK).body(articleService.findByTitleAll(boardId,keyword));
            }
        }
    }

    @GetMapping("/usr/article/detail")
    ResponseEntity<Object> detailBoard(@RequestParam("id")Long id){
        Optional<Article> temp = articleService.findById(id);

        if(temp.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArticleFindException("?????? ???????????? ???????????? ????????????."));
        }
        else{
            Article article =temp.get();
            article.setViews(temp.get().getViews()+1);
            articleService.viewsInc(article);
            return ResponseEntity.status(HttpStatus.OK).body(articleService.findById(id));
        }

    }

    @GetMapping("/usr/article/modify")
    ResponseEntity<Object> updateBoard(@RequestParam("id")Long id, Model model){
        ArticleModifyForm articleModifyForm = new ArticleModifyForm();
        articleModifyForm.setId(id);
        model.addAttribute("articleModifyForm",articleModifyForm);
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @PostMapping("/usr/article/doModify")
    ResponseEntity<Object> doUpdate(@RequestParam("id")Long id, @RequestParam("title")String title, @RequestParam("body")String content, HttpServletRequest request){
        Optional<Article> board = articleService.findById(id);
        HttpSession session = request.getSession(false); //?????? ???????????? ?????? ???????????? ????????? ????????? ?????????
        if(session == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArticleUpdateException("?????? ????????? ????????? ????????????."));
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Long memberId = loginMember.getId(); //session?????? ?????? ?????? ???????????? ???????????? id??? ?????????

        if(board.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArticleUpdateException("id??? ???????????? ???????????? ???????????? ????????????."));
        }
        else{
            Article temp = board.get();
            if(temp.getMemberId()==memberId){ //???????????? ???????????? ??????????????? ?????? ?????? ????????????
                ArticleModifyForm boardUpdateForm = new ArticleModifyForm();
                boardUpdateForm.setId(id);
                boardUpdateForm.setBody(content);
                boardUpdateForm.setTitle(title);
                articleService.update(boardUpdateForm);
                return ResponseEntity.status(HttpStatus.OK).body(board);
            }
            else{ //?????? ?????? ?????? ??????
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArticleUpdateException("?????? ???????????? ???????????? ????????????."));
            }
        }
    }

    @GetMapping("/usr/article/write")
    ResponseEntity<Object> writeBoard(@RequestParam("boardId")int boardId,Model model){
        ArticleSaveForm articleSaveForm = new ArticleSaveForm();

        articleSaveForm.setBoardId(boardId);
        model.addAttribute("articleSaveForm", articleSaveForm);

        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @PostMapping("/usr/article/doWrite")
    ResponseEntity<Object> doWrite(@RequestParam("boardId")int boardId, @RequestParam("title")String title, @RequestParam("body")String body, HttpServletRequest request){

        HttpSession session = request.getSession(false); //?????? ????????? ??? ???????????? id?????? ?????????
        if(session == null){
            return null;
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Long memberId = loginMember.getId();

        if(memberId==2){
            if(boardId==1){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArticleUpdateException("????????? ????????? ??? ?????? ????????? ?????????."));
            }
        }
        ArticleSaveForm articleSaveForm = new ArticleSaveForm();
        articleSaveForm.setBoardId(boardId);
        articleSaveForm.setTitle(title);
        articleSaveForm.setBody(body);

        return ResponseEntity.status(HttpStatus.OK).body(articleService.save(articleSaveForm,memberId));
    }
}
