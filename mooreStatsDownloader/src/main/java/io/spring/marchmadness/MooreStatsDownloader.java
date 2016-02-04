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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Retrieves NCAA Stats from USA Today and writes out a csv file with the results.
 * @author Glenn Renfro
 */
@SpringBootApplication
@EnableTask
public class MooreStatsDownloader {

	private static final String DELIMITER = ",";

	@Bean
	public DownloaderTask downLoaderTask() {
		return new DownloaderTask();
	}

	public static void main(String[] args) {
		SpringApplication.run(MooreStatsDownloader.class, args);
	}

	public static class DownloaderTask implements CommandLineRunner {

		private boolean isDataSetComplete = false;


		@Value("${statistics.url:http://sonnymoorepowerratings.com/m-basket.htm}")
		private String statisticsUrl;

		@Value("${output.filename:moore.csv}")
		private String outputFileName;

		@Override
		public void run(String... strings) throws Exception {
			retrieveStats();
		}

		private void retrieveStats() throws IOException {
			RestTemplate restTemplate = new RestTemplate();
			String s = restTemplate.getForObject(statisticsUrl, String.class);
			try (final FileWriter fw = new FileWriter(outputFileName)) {

				//Prepare data
				String[] rawStatistics = s.split("\n");

				//create the stream
				Stream<String> lines = Arrays.asList(rawStatistics).stream();

				//write statistics data
				lines.filter(line -> !isDataSetComplete && (line.startsWith(" ") ||
						line.startsWith("<A") || Character.isDigit(line.charAt(0)))).
						filter(line -> !line.contains("<TITLE>")).
						filter(line -> !line.contains("Teams:")).
						filter(line -> !line.trim().isEmpty()).
						forEach(line -> writeToCsvFile(fw, line));

			}
		}
		private void writeToCsvFile(FileWriter fw, String line) {
			try {
				if (line.startsWith("<A name=\"1alpha\">Alphabetical Listing</a><P></B>")){
					isDataSetComplete = true;
					return;
				}
				fw.write(String.format("%s%n", makeCommaDelimitedLine(line)));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private String makeCommaDelimitedLine(String line){
			String result = "";
			String[] tokens = StringUtils.tokenizeToStringArray(line," ",true,false);
			int counter = 0;
			for(String token : tokens){
				if(counter == 0){
					result = token;
				}
				else if(counter == 1){
					result = result + DELIMITER + token;
				}
				else if((Character.isAlphabetic(token.charAt(0)) ||
						token.charAt(0) == '&')&& counter > 1){
					result = result + " " + token;
				}
				else {
					result = result + DELIMITER + token;
				}
				counter++;
			}
			return result;
		}

	}
}
