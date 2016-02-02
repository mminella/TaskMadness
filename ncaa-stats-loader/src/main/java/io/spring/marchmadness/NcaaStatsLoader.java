/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.marchmadness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Retrieves NCAA Stats from USA Today and writes out a csv file with the results.
 * @author Glenn Renfro
 */
@SpringBootApplication
@EnableTask
public class NcaaStatsLoader {

	private static final String DELIMITER = ",";

	@Bean
	public LoaderTask LoaderTask() {
		return new LoaderTask();
	}

	public static void main(String[] args) {
		SpringApplication.run(NcaaStatsLoader.class, args);
	}

	public static class LoaderTask implements CommandLineRunner {

		@Value("${input.filename:output.csv}")
		private String inputFileName;

		@Autowired
		ResourceLoader resourceLoader;

		@Override
		public void run(String... strings) throws Exception {
			//loadStats();
		}

		private void loadStats() throws IOException{
			Resource resource = resourceLoader.getResource("file:"+inputFileName);
			File csvFile = resource.getFile();
			FileReader reader = new FileReader(csvFile);
			BufferedReader br = new BufferedReader(reader);
			String s;
			while((s = br.readLine()) != null) {
				System.out.println(s);
			}
			reader.close();
		}

	}
}
