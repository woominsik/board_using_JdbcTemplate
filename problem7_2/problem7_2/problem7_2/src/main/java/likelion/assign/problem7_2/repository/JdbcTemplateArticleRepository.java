package likelion.assign.problem7_2.repository;

import likelion.assign.problem7_2.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateArticleRepository implements ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateArticleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Long save(Article article) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("article").usingGeneratedKeyColumns("id");
        //파라미터 바인딩
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", article.getTitle());
        parameters.put("body", article.getBody());
        parameters.put("views", article.getViews());
        parameters.put("recommended", article.getRecommended());
        parameters.put("boardId", article.getBoardId());
        parameters.put("written", article.getWritten());
        parameters.put("memberId", article.getMemberId());

        // 실행 & db 에서 생성된 key 받아오기
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        article.setId(key.longValue());
        return article.getId();
    }

    @Override
    public Optional<Article> findById(Long id) {
        List<Article> result = jdbcTemplate.query("select * from article where id = ?", articleRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public List<Article> findByTitleAll(int boardId, String title) {
        String wrappedKeyword = "%"+title+"%";
        return jdbcTemplate
                .query("select * from article where boardId=? AND title LIKE ? limit 10", articleRowMapper(),boardId,wrappedKeyword);
    }

    @Override
    public List<Article> findAll(int boardId) {
        return jdbcTemplate
                .query("select * from article where boardId= ? limit 10", articleRowMapper(),boardId);

    }

    @Override
    public List<Article> findAll(int boardId, int PageNum) {
        int idNum = (PageNum-1)*10;
        return jdbcTemplate
                .query("select * from article where boardId= ? AND id>= ? AND id< ? limit 10", articleRowMapper(),boardId, idNum+1, idNum+11);

    }

    @Override
    public List<Article> findAllRecent(int boardId) {
        return jdbcTemplate
                .query("select * from article where boardId= ? ORDER BY id DESC limit 10", articleRowMapper(),boardId);

    }

    @Override
    public void update(Article article) {
        jdbcTemplate.update("update article set title = ?, body = ?, lastModified = ?, views = ? where id = ?", article.getTitle(), article.getBody(),article.getLastModified(), article.getViews(), article.getId());
    }

    private RowMapper<Article> articleRowMapper() {
        return (rs, rowNum) -> {
            Article article = new Article();
            article.setId(rs.getLong("id"));
            article.setTitle(rs.getString("title"));
            article.setBody(rs.getString("body"));
            article.setViews(rs.getLong("views"));
            article.setRecommended(rs.getLong("recommended"));
            article.setBoardId(rs.getInt("boardId"));
            article.setLastModified(rs.getString("lastModified"));
            article.setWritten(rs.getString("written"));
            article.setMemberId(rs.getLong("memberId"));
            return article;
        };
    }
}
