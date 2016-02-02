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
import org.springframework.web.client.RestTemplate;

/**
 * Retrieves NCAA Stats from USA Today and writes out a csv file with the results.
 * @author Glenn Renfro
 */
@SpringBootApplication
@EnableTask
public class NcaaStatsDownloader {

	private static final String DELIMITER = ",";

	@Bean
	public DownloaderTask downLoaderTask() {
		return new DownloaderTask();
	}

	public static void main(String[] args) {
		SpringApplication.run(NcaaStatsDownloader.class, args);
	}

	public static class DownloaderTask implements CommandLineRunner {

		@Value("${statistics.url:http://www.usatoday.com/sports/ncaab/sagarin/2016/team/}")
		private String statisticsUrl;

		@Value("${output.filename:output.csv}")
		private String outputFileName;

		@Override
		public void run(String... strings) throws Exception {
			retrieveStats();
		}

		private void retrieveStats() throws IOException{
			RestTemplate restTemplate = new RestTemplate();
			String s = restTemplate.getForObject(statisticsUrl, String.class);
			final FileWriter fw = new FileWriter(outputFileName);

			s = s.replace("br","\n");
			//Means that the data came from years 2014 to present and has different format
			if(s.contains("GOLDEN_MEAN")){
				s = prepStatsWithGoldenMean(s);
				writeGoldenMeanHeaders(fw);
			}
			else{ //Years 2013 and does not contain GoldenMean
				s = prepStatsWithNoGoldenMean(s);
				writeStandardHeaders(fw);
			}

			String[] rawStatistics = s.split("\n");
			Stream<String> lines = Arrays.asList(rawStatistics).stream();

			//write statistics data
			lines.filter(line ->line.startsWith(" ")).
					filter(line->line.contains(" = ")).
					filter(line->!line.startsWith("    ")).
					forEach(line->writeToCsvFile(fw, line));

			fw.close();
		}

		private String prepStatsWithGoldenMean(String s){
			s = s.replaceAll("<font color=\"#.+?>&nbsp"," ");
			return s.replaceAll("</font>|\\&nbsp|<|>|\\||;|\\(|\\)","");
		}

		private String prepStatsWithNoGoldenMean(String s){
			s = s.replaceAll(">&nbsp"," ");
			return s.replaceAll("</font>|\\&nbsp|<|>|\\||COLOR=\\\"#9900ff\\\"|COLOR=\\\"#ff0000\\\"|COLOR=\\\"#0000ff\\\"|font|/|;|\\(|\\)","");
		}

		private void writeToCsvFile(FileWriter fw, String line) {
			try {
				fw.write(String.format("%s%n", makeCommaDelimitedLine(line)));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		private String makeCommaDelimitedLine(String line){
			String result = "";
			String[] tokens = line.trim().replaceAll(" +", " ").split(" ");
			boolean isEqualFound = false;
			int counter = 0;
			for(String token : tokens){
				if(token.equals("=")){
					isEqualFound = true;
				}
				else if(!isEqualFound && counter == 0){
					result = token;
				}
				else if(!isEqualFound && counter == 1){
					result = result + DELIMITER + token;
				}
				else if(!isEqualFound && counter > 1){
					result = result + " " + token;
				}
				else if(isEqualFound){
					result = result + DELIMITER + token;
				}
				counter++;
			}
			return result;
		}

		private void writeGoldenMeanHeaders(FileWriter fw) throws IOException{
			fw.write(String.format("%s%n", "rank,name,rating,win,loss,schedltop25,schedltop25rank,top25win,top25loss,top50win,top50loss,predictor,predictor_rank,golden_mean,golden_mean_rank,recent/elo,recent/elo_Rank"));
		}
		private void writeStandardHeaders(FileWriter fw) throws IOException{
			fw.write(String.format("%s%n", "rank,name,rating,win,loss,schedltop25,schedltop25rank,top25win,top25loss,top50win,top50loss,elo,elo_Rank,predictor,predictor_rank"));
		}


	}
}
