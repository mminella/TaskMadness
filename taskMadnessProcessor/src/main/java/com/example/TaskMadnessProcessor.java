package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.task.launcher.TaskLaunchRequest;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
@EnableBinding(Processor.class)
public class TaskMadnessProcessor {

	public static void main(String[] args) {
		SpringApplication.run(TaskMadnessProcessor.class, args);
	}

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object setupRequest(TaskExecution taskExecution) {
		System.out.println("************ Arguments Size = " + taskExecution.getArguments().size());
		for(String args :taskExecution.getArguments()) {
			System.out.println("************  " + args);
		}
		Map<String, String> properties = new HashMap<String,String>();
		String artifact = null;
		properties.put("spring_datasource_url","jdbc:mariadb://localhost:3306/practice");
		properties.put("spring_datasource_driverClassName", "org.mariadb.jdbc.Driver");
		properties.put("spring_datasource_username", "root");
		properties.put("spring_datasource_password","password");
		artifact = "maven://io.spring.marchmadness:bracket-scorer:0.0.1-SNAPSHOT";
		System.out.println("gotcha" + taskExecution.getExecutionId() + " " + taskExecution.getTaskName());
		TaskLaunchRequest request = new TaskLaunchRequest(artifact, null, properties, null);
		return new GenericMessage<TaskLaunchRequest>(request);
	}
}
