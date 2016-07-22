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

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Glenn Renfro
 */
@Configuration
@EnableBatchProcessing
public class MooreStatConfiguration {

	@Value("${input.filename:moore.csv}")
	private String inputFileName;

	@Bean
	public ItemReader<MooreNcaaStat> reader(ResourceLoader resourceLoader) {
		FlatFileItemReader<MooreNcaaStat> reader = new FlatFileItemReader<MooreNcaaStat>();
		reader.setResource(resourceLoader.getResource("file:" + inputFileName));
		reader.setLineMapper(new DefaultLineMapper<MooreNcaaStat>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[]{"year", "rank", "name", "win", "loss", "tie",
						"sos", "pr" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<MooreNcaaStat>() {{
				setTargetType(MooreNcaaStat.class);
			}});
		}});
		return reader;
	}


	@Bean
	public ItemWriter<MooreNcaaStat> writer(DataSource dataSource) {
		JdbcBatchItemWriter<MooreNcaaStat> writer = new JdbcBatchItemWriter<MooreNcaaStat>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<MooreNcaaStat>());
		writer.setSql("INSERT INTO MOORE_NCAA_STATS (YEAR, RANK, " +
				"NAME, " +
				"WIN, " +
				"LOSS, " +
				"TIE, " +
				"SOS, " +
				"PR ) VALUES (:year, :rank, :name, :win, :loss, :tie, :sos, :pr)");
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener jobExecutionEventsListener) {
		return jobs.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(jobExecutionEventsListener)
				.flow(s1)
				.end()
				.build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<MooreNcaaStat> reader,
					  ItemWriter<MooreNcaaStat> writer) {
		return stepBuilderFactory.get("step1")
				.<MooreNcaaStat, MooreNcaaStat>chunk(10)
				.reader(reader)
				.writer(writer)
				.build();
	}

	@Bean
	public MooreStatsDownloader mooreStatsDownloader() {
		return new MooreStatsDownloader();
	}

}
