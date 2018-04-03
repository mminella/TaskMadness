package io.spring.marchmadness;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class KenpomDownloader {

	public static void main(String[] args) {
		SpringApplication.run(KenpomDownloader.class, args);
	}

	@Component
	public static class DownloaderTask implements CommandLineRunner {

		// 2016
//		private Pattern pattern = Pattern.compile("<tr(\\ class=\\\"tourney\\\")?><td(\\ class=\"bold-bottom\")?>(\\d+).*team=[\\w\\+\\.%]+\\\">([\\w\\+\\.\\ \\&amp\\;]+).*php\\?c=\\w+\\\">\\w+<\\/a><\\/td><td( class=\\\"[\\w- ]+\\\")?>(\\d+-\\d+)<\\/td><td( class=\\\"[\\w- ]+\\\")?>(\\.\\d+)<\\/td>(<td( class=\\\"[\\w- ]+\\\")?>(<span class=\"[\\w]+\">)?[\\d\\w\\.\\+\\-]+(<\\/span>)?<\\/td>){8}<td class=\"[\\w-]+\">(\\.\\d+)");
		// 2015
//		private Pattern pattern = Pattern.compile("<tr(\\ class=\\\"tourney\\\")?><td(\\ class=\"bold-bottom\")?>(\\d+).*team=[\\w\\+\\.%]+\\&y=2015\\\">([\\w\\+\\.\\ \\&amp\\;]+).*php\\?c=\\w+\\&y=2015\\\">\\w+<\\/a><\\/td><td( class=\\\"[\\w- ]+\\\")?>(\\d+-\\d+)<\\/td><td( class=\\\"[\\w- ]+\\\")?>(\\.\\d+)<\\/td>(<td( class=\\\"[\\w- ]+\\\")?>(<span class=\"[\\w]+\">)?[\\d\\w\\.\\+\\-]+(<\\/span>)?<\\/td>){8}<td class=\"[\\w-]+\">(\\.\\d+)");
		// 2018
		private Pattern pattern = Pattern.compile("<tr(\\ class=\\\"tourney\\\")?><td(\\ class=\"bold-bottom\")?>(\\d+).*team=[\\w\\+\\.%]+\\\">([\\w\\+\\.\\ \\&amp\\;]+).*php\\?c=\\w+\\\">\\w+<\\/a><\\/td><td( class=\\\"[\\w- ]+\\\")?>(\\d+-\\d+)<\\/td><td( class=\\\"[\\w- ]+\\\")?>([+-]\\d+\\.\\d+)<\\/td>(<td( class=\\\"[\\w- ]+\\\")?>(<span class=\"[\\w]+\">)?[\\d\\w\\.\\+\\-]+(<\\/span>)?<\\/td>){8}<td class=\"[\\w- ]+\">([+-]\\d+\\.\\d+)");

		@Value("${statistics.url:https://kenpom.com/}")
		private String statisticsUrl;

		@Value("${output.filename:kenpom.csv}")
		private String outputFileName;

		@Override
		public void run(String... strings) throws Exception {
			retrieveStats();
		}

		private void retrieveStats() throws IOException {
			RestTemplate restTemplate = new RestTemplate();
			String s = restTemplate.getForObject(statisticsUrl, String.class);
			System.out.println(">> s = " + s);
			try (final FileWriter fw = new FileWriter(outputFileName)) {
				//Prepare data
				String[] rawStatistics = s.split("\n");

				String[] tempData = new String[479];

				// 2015
				// System.arraycopy(rawStatistics, 142, tempData, 0, 479);

				// 2016
//				System.arraycopy(rawStatistics, 143, tempData, 0, 479);

				// 2018
				System.arraycopy(rawStatistics, 143, tempData, 0, 479);

				//create the stream
				Stream<String> lines = Arrays.asList(tempData).stream();

				//write statistics data
				lines.filter(line -> line.length() > 300)
						.forEach(line -> writeToCsvFile(fw, line));

			}
		}

		private void writeToCsvFile(FileWriter fw, String line) {
			try {
				fw.write(String.format("%s\n", makeCommaDelimitedLine(line)));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private String makeCommaDelimitedLine(String line){
			Matcher matcher = this.pattern.matcher(line);

			if(matcher.find()) {
				String winsLosses = matcher.group(6);

				String[] split = winsLosses.split("-");

				return matcher.group(3) + "," + matcher.group(4) + "," + split[0] + "," + split[1] + "," + matcher.group(13) + "," + matcher.group(8);
			}
			else {
				return line;
			}
		}
	}
}
