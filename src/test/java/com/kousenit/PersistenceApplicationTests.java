package com.kousenit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersistenceApplicationTests {
    @Autowired
    private ApplicationContext ctx;

	@Test
	public void contextLoads() {
        Stream.of(ctx.getBeanDefinitionNames())
                .filter(name -> name.contains("TransactionManager"))
                .forEach(System.out::println);
	}

}
