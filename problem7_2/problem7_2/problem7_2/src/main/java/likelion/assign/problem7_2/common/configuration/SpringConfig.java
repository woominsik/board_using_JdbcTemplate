package likelion.assign.problem7_2.common.configuration;

import likelion.assign.problem7_2.repository.ArticleRepository;
import likelion.assign.problem7_2.repository.JdbcTemplateArticleRepository;
import likelion.assign.problem7_2.repository.JdbcTemplateMemberRepository;
import likelion.assign.problem7_2.repository.MemberRepository;
import likelion.assign.problem7_2.service.ArticleService;
import likelion.assign.problem7_2.service.MemberService;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public ArticleRepository articleRepository(){
        return new JdbcTemplateArticleRepository(dataSource);
    }


    public MemberRepository memberRepository(){
        return new JdbcTemplateMemberRepository(dataSource);
    }

    public ArticleService boardService(){
        return new ArticleService(articleRepository());
    }


    public MemberService memberService(){
        return new MemberService(memberRepository());
    }
}
