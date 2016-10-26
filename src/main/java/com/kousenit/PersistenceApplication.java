package com.kousenit;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class PersistenceApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(PersistenceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        List<Object[]> nameArrays = Stream.of("CAPTAIN James Kirk", "CAPTAIN Jean-Luc Picard",
                "COMMANDER Benjamin Sisko", "CAPTAIN Katheryn Janeway",
                "CAPTAIN Jonathan Archer")
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        template.batchUpdate("INSERT INTO officers(rank, first_name, last_name) " +
                "VALUES(?, ?, ?)", nameArrays);

        template.query("SELECT * FROM officers",
                (rs, num) -> new Officer(rs.getInt("id"),
                        Rank.valueOf(rs.getString("rank")),
                        rs.getString("first_name"),
                        rs.getString("last_name")))
                .forEach(System.out::println);
    }
}
