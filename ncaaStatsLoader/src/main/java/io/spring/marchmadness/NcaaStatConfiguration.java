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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Glenn Renfro
 */
@Configuration
@EnableBatchProcessing
public class NcaaStatConfiguration {

	@Value("${input.filename:output.csv}")
	private String inputFileName;

	@Bean
	public ItemReader<NcaaStats> reader(ResourceLoader resourceLoader) {
		FlatFileItemReader<NcaaStats> reader = new FlatFileItemReader<NcaaStats>();
		reader.setResource(resourceLoader.getResource("file:" + inputFileName));
		reader.setLineMapper(new DefaultLineMapper<NcaaStats>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[]{ "year", "rank", "name", "rating", "win", "loss", "schedl",
						"schedlRank", "winTop25", "lossTop25", "winTop50", "lossTop50",
						"predictor", "predictorRank", "goldenMean", "goldenMeanRank",
						"recent", "recentRank" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<NcaaStats>() {{
				setTargetType(NcaaStats.class);
			}});
		}});
		return reader;
	}

	@Bean
	public ItemProcessor<NcaaStats, NcaaStats> processor() {
		return new NcaaItemProcessor();
	}

	@Bean
	public ItemWriter<NcaaStats> writer(DataSource dataSource) {
		JdbcBatchItemWriter<NcaaStats> writer = new JdbcBatchItemWriter<NcaaStats>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<NcaaStats>());
		writer.setSql("INSERT INTO NCAA_STATS (YEAR," +
				"RANK, " +
				"NAME, " +
				"RATING, " +
				"WIN, " +
				"LOSS, " +
				"SCHEDL, " +
				"SCHEDL_RANK, " +
				"WIN_TOP_25, " +
				"LOSS_TOP_25, " +
				"WIN_TOP_50, " +
				"LOSS_TOP_50, " +
				"PREDICTOR, " +
				"PREDICTOR_RANK, " +
				"GOLDEN_MEAN, " +
				"GOLDEN_MEAN_RANK, " +
				"RECENT," +
				"RECENT_RANK) VALUES (:year, :rank, :name, :rating, :win, :loss, " +
				":schedl, :schedlRank, :winTop25, :lossTop25, :winTop50, :lossTop50, " +
				":predictor, :predictorRank, :goldenMean, :goldenMeanRank, :recent, " +
				":recentRank)");
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
	public Step step1(StepBuilderFactory stepBuilderFactory,
					  ItemReader<NcaaStats> reader,
					  ItemWriter<NcaaStats> writer,
					  ItemProcessor<NcaaStats,NcaaStats> processor) {
		return stepBuilderFactory.get("step1")
				.<NcaaStats, NcaaStats>chunk(10)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}

	@Bean
	public NcaaStatsDownloader ncaaStatsDownloader() {
		return new NcaaStatsDownloader();
	}

}
