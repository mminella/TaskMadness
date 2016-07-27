package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.task.launcher.TaskLaunchRequest;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableConfigurationProperties({TaskMadnessProperties.class})
public class TaskMadnessProcessor {

	public static void main(String[] args) {
		SpringApplication.run(TaskMadnessProcessor.class, args);
	}

	@Autowired
	TaskMadnessProperties taskMadnessProperties;

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object setupRequest(TaskExecution taskExecution) {
		Map<String, String> properties = new HashMap<String,String>();
		String artifact = null;
		properties.put("spring_datasource_url", taskMadnessProperties.getDatabaseUrl());
		properties.put("spring_datasource_driverClassName", taskMadnessProperties.getDriverClassName());
		properties.put("spring_datasource_username", taskMadnessProperties.getUserName());
		properties.put("spring_datasource_password",taskMadnessProperties.getPassword());
		artifact = "maven://io.spring.marchmadness:bracket-scorer:0.0.1-SNAPSHOT";
		TaskLaunchRequest request = new TaskLaunchRequest(artifact, null, properties, null);
		return new GenericMessage<TaskLaunchRequest>(request);
	}
}
