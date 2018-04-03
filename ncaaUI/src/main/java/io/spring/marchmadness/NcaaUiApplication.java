package io.spring.marchmadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/teams2018.properties")
public class NcaaUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NcaaUiApplication.class, args);
	}
}
