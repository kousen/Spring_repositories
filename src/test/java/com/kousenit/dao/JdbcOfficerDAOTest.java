package com.kousenit.dao;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JdbcOfficerDAOTest {
    @Autowired @Qualifier("jdbcOfficerDAO")
    private OfficerDAO dao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Officer> officers =
            Arrays.asList(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                    new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                    new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                    new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                    new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @Before
    public void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS officers");
        jdbcTemplate.execute("CREATE TABLE officers(\n" +
                "    id INT NOT NULL AUTO_INCREMENT,\n" +
                "    rank VARCHAR(20) NOT NULL, first_name VARCHAR(50) NOT NULL,\n" +
                "    last_name VARCHAR(20) NOT NULL,\n" +
                "    PRIMARY KEY(id)\n" +
                ")");
        officers.forEach(officer -> dao.save(officer));
    }

//    @After
//    public void tearDown() {
//        jdbcTemplate.execute("DROP TABLE IF EXISTS officers");
//    }

    @Test
    public void save() throws Exception {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = dao.save(officer);
        assertNotNull(officer.getId());
        assertEquals(Rank.LIEUTENANT, officer.getRank());
        assertEquals("Nyota", officer.getFirst());
        assertEquals("Uhuru", officer.getLast());
    }

    @Test
    public void findOne() throws Exception {
        Officer officer = dao.findOne(1);
        assertNotNull(officer);
        assertEquals(1, officer.getId().intValue());
    }

    @Test
    public void findAll() throws Exception {
        List<String> dbNames = dao.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count() throws Exception {
        assertEquals(5, dao.count().longValue());
    }

    @Test
    public void delete() throws Exception {
        IntStream.rangeClosed(1, 5)
                .forEach(id -> dao.delete(dao.findOne(id)));
        assertEquals(0, dao.count().longValue());
    }

    @Test
    public void exists() throws Exception {
        IntStream.rangeClosed(1, 5)
                .forEach(id -> assertTrue(String.format("%d should exist", id), dao.exists(id)));
    }

}