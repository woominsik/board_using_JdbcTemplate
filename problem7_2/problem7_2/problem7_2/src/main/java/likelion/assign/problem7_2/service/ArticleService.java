package likelion.assign.problem7_2.service;

import likelion.assign.problem7_2.domain.Article;
import likelion.assign.problem7_2.domain.form.ArticleModifyForm;
import likelion.assign.problem7_2.domain.form.ArticleSaveForm;
import likelion.assign.problem7_2.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    public Article save(Article article){
        articleRepository.save(article);
        return article;
    }

    public Article save(ArticleSaveForm saveForm, Long memberId){
        Article article = new Article();
        article.setBoardId(saveForm.getBoardId());
        article.setTitle(saveForm.getTitle());
        article.setBody(saveForm.getBody());
        article.setRecommended(0L);
        article.setViews(0L);
        article.setMemberId(memberId);
        article.setWritten(LocalDateTime.now().toString());

        articleRepository.save(article);
        return article;
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }

    public List<Article> findByTitleAll(int boardId, String title){
        return articleRepository.findByTitleAll(boardId, title);
    }
    public List<Article> findAll(int boardId){
        return articleRepository.findAll(boardId);
    }

    public List<Article> findAll(int boardId, int PageNum){
        return articleRepository.findAll(boardId,PageNum);
    }

    public List<Article> findAllRecent(){
        List<Article> temp = new ArrayList<>();
        temp.addAll(articleRepository.findAllRecent(1));
        temp.addAll(articleRepository.findAllRecent(2));
        return temp;
    }

    public void viewsInc(Article article){
        articleRepository.update(article);
    }

    public Article update(ArticleModifyForm articleModifyForm){
        Article temp = new Article();
        temp.setId(articleModifyForm.getId());
        temp.setLastModified(LocalDateTime.now().toString());
        temp.setBody(articleModifyForm.getBody());
        temp.setTitle(articleModifyForm.getTitle());
        articleRepository.update(temp);
        return temp;
    }
}
