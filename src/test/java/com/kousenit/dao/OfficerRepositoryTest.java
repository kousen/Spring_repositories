package com.kousenit.dao;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OfficerRepositoryTest {
    @Autowired
    private OfficerRepository repository;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void testSave() throws Exception {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = repository.save(officer);
        assertNotNull(officer.getId());
        assertEquals(Rank.LIEUTENANT, officer.getRank());
        assertEquals("Nyota", officer.getFirst());
        assertEquals("Uhuru", officer.getLast());
    }

    @Test
    public void findOne() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
                .forEach(id -> {
                    Officer officer = repository.findOne(id);
                    assertNotNull(officer);
                    assertEquals(id, officer.getId());
                });
    }

    @Test
    public void findAll() throws Exception {
        List<String> dbNames = repository.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count() throws Exception {
        assertEquals(5, repository.count());
    }

    @Test
    public void delete() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
                .forEach(id -> repository.delete(repository.findOne(id)));
        assertEquals(0, repository.count());
    }

    @Test
    public void exists() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
                .forEach(id -> assertTrue(String.format("%d should exist", id),
                        repository.exists(id)));
    }

    @Test
    public void findByRank() throws Exception {
        repository.findByRank(Rank.CAPTAIN).forEach(captain ->
                assertEquals(Rank.CAPTAIN, captain.getRank()));

    }

    @Test
    public void findByLast() throws Exception {
        List<String> lastNames = Arrays.asList("Kirk", "Picard", "Sisko", "Janeway", "Archer");
        lastNames.forEach(lastName ->
                assertEquals(lastName,
                        repository.findByLast(lastName).orElseGet(Officer::new).getLast()));
    }
}