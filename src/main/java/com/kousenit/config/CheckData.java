package com.kousenit.config;

import com.kousenit.dao.OfficerDAO;
import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CheckData implements CommandLineRunner {
    @Autowired
    private OfficerDAO dao;

    @Override
    public void run(String... args) throws Exception {
        dao.findAll().forEach(System.out::println);
        Officer spock = dao.save(new Officer(Rank.COMMANDER, "", "Spock"));
        System.out.println(spock);
        dao.delete(spock);
    }
}
