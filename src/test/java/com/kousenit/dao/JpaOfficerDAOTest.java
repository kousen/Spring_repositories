package com.kousenit.dao;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JpaOfficerDAOTest {
    @Autowired @Qualifier("jpaOfficerDAO")
    private OfficerDAO dao;

    private List<Officer> officers =
            Arrays.asList(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                    new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                    new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                    new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                    new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @Test
    public void testSave() throws Exception {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = dao.save(officer);
        assertNotNull(officer.getId());
        assertEquals(Rank.LIEUTENANT, officer.getRank());
        assertEquals("Nyota", officer.getFirst());
        assertEquals("Uhuru", officer.getLast());
    }

    @Test
    public void findOne() throws Exception {
        officers.stream()
                .forEach(officer -> dao.save(officer));
        officers.stream()
                .mapToInt(Officer::getId)
                .forEach(id -> {
                    Officer officer = dao.findOne(id);
                    assertNotNull(officer);
                    assertEquals(id, officer.getId().intValue());
                });
    }

    @Test
    public void findAll() throws Exception {
        officers.stream()
                .forEach(officer -> dao.save(officer));
        List<String> dbNames = dao.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count() throws Exception {
        officers.stream()
                .forEach(officer -> dao.save(officer));
        assertEquals(5, dao.count().longValue());
    }

    @Test
    public void delete() throws Exception {
        officers.stream()
                .forEach(officer -> dao.save(officer));
        officers.stream()
                .mapToInt(Officer::getId)
                .forEach(id -> dao.delete(dao.findOne(id)));
        assertEquals(0, dao.count().longValue());
    }

    @Test
    public void exists() throws Exception {
        officers.stream()
                .forEach(officer -> dao.save(officer));
        officers.stream()
                .mapToInt(Officer::getId)
                .forEach(id -> assertTrue(String.format("%d should exist", id),
                        dao.exists(id)));
    }
}