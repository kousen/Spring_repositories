package com.kousenit.config;

import com.kousenit.dao.OfficerRepository;
import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CheckData implements CommandLineRunner {
    @Autowired
    private OfficerRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.findAll().forEach(System.out::println);
        Officer spock = repository.save(new Officer(Rank.COMMANDER, "", "Spock"));
        System.out.println(spock);
        repository.delete(spock);
    }
}
