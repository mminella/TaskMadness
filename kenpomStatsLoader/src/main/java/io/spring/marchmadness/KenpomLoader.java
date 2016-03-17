package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@SpringBootApplication
@EnableTask
public class KenpomLoader {

	public static void main(String[] args) {
		SpringApplication.run(KenpomLoader.class, args);
	}
}
