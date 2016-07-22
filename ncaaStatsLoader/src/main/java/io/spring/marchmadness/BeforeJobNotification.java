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

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Glenn Renfro
 */
@Component
public class BeforeJobNotification {

	@Value("${input.filename:output.csv}")
	private String inputFile;

	@Autowired
	List<DataSource> dataSource;

	@Autowired
	NcaaStatsDownloader ncaaStatsDownloader;

	@BeforeTask
	public void beforeTask(TaskExecution taskExecution) throws IOException {
		ncaaStatsDownloader.retrieveStats();
		JdbcTemplate template = new JdbcTemplate(dataSource.get(0));
		getYearFromData();
		template.execute("delete from NCAA_STATS where year = " + getYearFromData());
	}

	private String getYearFromData(){
		String result = null;
		try (FileReader reader = new FileReader(inputFile)){
			char[] charArray = new char[4];
			reader.read(charArray,0,4);
			result = new String(charArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
