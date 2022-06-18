package likelion.assign.problem7_2.repository;

import likelion.assign.problem7_2.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("loginId", member.getLoginId());
        parameters.put("loginPw", member.getLoginPw());
        parameters.put("memberRole", member.getMemberRole());
        // 실행 & db 에서 생성된 key 받아오기
        System.out.println(parameters);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member.getId();
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", userRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        List<Member> result = jdbcTemplate.query("select * from member where loginId = ?", userRowMapper(), loginId);
        return result.stream().findAny();
    }


    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", userRowMapper());
    }

    private RowMapper<Member> userRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("loginId"));
            member.setLoginPw(rs.getString("loginPw"));
            member.setMemberRole(rs.getInt("memberRole"));
            return member;
        };
    }
}
