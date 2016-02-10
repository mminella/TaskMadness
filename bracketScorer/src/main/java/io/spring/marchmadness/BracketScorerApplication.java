package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableTask
//@Import(TaskConfiguration.class)
@PropertySource("classpath:/teams.properties")
public class BracketScorerApplication {

	public static void main(String[] args) {
		SpringApplication.exit(SpringApplication.run(BracketScorerApplication.class, args));
	}
}
