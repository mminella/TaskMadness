package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableTask
@PropertySource("classpath:/teams2016.properties")
public class BracketScorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BracketScorerApplication.class, args);
	}
}
