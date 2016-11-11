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
public class PersistenceApplication {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JdbcTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(PersistenceApplication.class, args);
	}
}
