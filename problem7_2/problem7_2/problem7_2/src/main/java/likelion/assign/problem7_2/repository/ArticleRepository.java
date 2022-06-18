package likelion.assign.problem7_2.repository;

import likelion.assign.problem7_2.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Long save(Article article);
    Optional<Article> findById(Long id);
    List<Article> findByTitleAll(int boardId, String title);
    List<Article> findAll(int boardId);
    List<Article> findAll(int boardId, int PageNum);
    List<Article> findAllRecent(int boardId);
    void update(Article article);
}
