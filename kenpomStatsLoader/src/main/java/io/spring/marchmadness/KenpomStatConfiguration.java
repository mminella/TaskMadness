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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Glenn Renfro
 */
@Configuration
@EnableBatchProcessing
public class KenpomStatConfiguration {

	@Value("${input.filename:kenpom.csv}")
	private String inputFileName;

	@Autowired
	private Environment env;

	@Bean
	public ItemReader<KenpomNcaaStat> reader(ResourceLoader resourceLoader) {
		FlatFileItemReader<KenpomNcaaStat> reader = new FlatFileItemReader<>();
		reader.setResource(resourceLoader.getResource("file:" + inputFileName));
		reader.setLineMapper(new DefaultLineMapper<KenpomNcaaStat>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[]{"rank", "name", "win", "loss",
						"sos", "pr" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<KenpomNcaaStat>() {{
				setTargetType(KenpomNcaaStat.class);
			}});
		}});
		return reader;
	}

	@Bean
	public ItemProcessor<KenpomNcaaStat, KenpomNcaaStat> itemProcessor() {
		return kenpomNcaaStat -> {
			String key = kenpomNcaaStat.getName().toUpperCase();
			key = key.replaceAll(" ", ".");

			if(env.containsProperty(key)) {
				kenpomNcaaStat.setName(env.getProperty(key).toUpperCase());
			}
			else {
				kenpomNcaaStat.setName(kenpomNcaaStat.getName().toUpperCase());
			}

			return kenpomNcaaStat;
		};
	}

	@Bean
	public ItemWriter<KenpomNcaaStat> writer(DataSource dataSource) {
		JdbcBatchItemWriter<KenpomNcaaStat> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO KENPOM_STATS (YEAR, RANK, " +
				"NAME, " +
				"WIN, " +
				"LOSS, " +
				"SOS, " +
				"PR ) VALUES (:year, :rank, :name, :win, :loss, :sos, :pr)");
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
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<KenpomNcaaStat> reader,
					  ItemWriter<KenpomNcaaStat> writer) {
		return stepBuilderFactory.get("step1")
				.<KenpomNcaaStat, KenpomNcaaStat>chunk(10)
				.reader(reader)
				.processor(itemProcessor())
				.writer(writer)
				.build();
	}

	@Bean
	public KenpomDownloader kenpomDownloader() {
		return new KenpomDownloader();
	}

}
