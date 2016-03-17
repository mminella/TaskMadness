package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableTask
@PropertySource(value = { "application.properties","KenpomNames.properties" })
public class KenpomLoader {

	public static void main(String[] args) {
		SpringApplication.run(KenpomLoader.class, args);
	}
}
