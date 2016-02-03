package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@SpringBootApplication
@EnableTask
public class BracketScorerApplication {

	public static void main(String[] args) {
		SpringApplication.exit(SpringApplication.run(BracketScorerApplication.class, args));
	}
}
