package com.kousenit.dao;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class JdbcOfficerDAO implements OfficerDAO {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertOfficer;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public JdbcOfficerDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        insertOfficer = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("officers")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Officer save(Officer officer) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("rank", officer.getRank())
                .addValue("first_name", officer.getFirst())
                .addValue("last_name", officer.getLast());
        Integer newId = (Integer) insertOfficer.executeAndReturnKey(parameters);
        officer.setId(newId);
        return officer;
    }

    @Override
    public Officer findOne(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM officers WHERE id=?",
                new RowMapper<Officer>() {  // Java 7 anonymous inner class
                    @Override
                    public Officer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Officer(rs.getInt("id"),
                                Rank.valueOf(rs.getString("rank")),
                                rs.getString("first_name"),
                                rs.getString("last_name"));
                    }
                },
                id);
    }

    @Override
    public Collection<Officer> findAll() {
        return jdbcTemplate.query("SELECT * FROM officers",
                (rs, rowNum) -> new Officer(rs.getInt("id"), // Java 8 lambda expression
                        Rank.valueOf(rs.getString("rank")),
                        rs.getString("first_name"),
                        rs.getString("last_name")));
    }

    @Override
    public Long count() {
        return jdbcTemplate.queryForObject("select count(*) from officers", Long.class);
    }

    @Override
    public void delete(Officer officer) {
        jdbcTemplate.update("DELETE FROM officers WHERE id=?", officer.getId());
    }

    @Override
    public boolean exists(Integer id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM officers where id=?)",
                Boolean.class, id);
    }
}
