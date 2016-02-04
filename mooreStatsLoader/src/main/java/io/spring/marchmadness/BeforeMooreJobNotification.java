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

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Glenn Renfro
 */
@Component
public class BeforeMooreJobNotification extends JobExecutionListenerSupport {

	@Value("${input.filename:moore.csv}")
	private String inputFile;

	@Autowired
	List<DataSource> dataSource;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		JdbcTemplate template = new JdbcTemplate(dataSource.get(0));
		template.execute("delete from MOORE_NCAA_STATS ");
	}

}
