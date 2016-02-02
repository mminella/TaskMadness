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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
		reader.setLinesToSkip(1);
		reader.setResource(resourceLoader.getResource("file:"+inputFileName));
		reader.setLineMapper(new DefaultLineMapper<NcaaStats>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] { "rank", "name", "rating", "win", "loss","schedl",
						"schedlRank","winTop25","lossTop25","winTop50","lossTop50",
						"predictor","predictorRank","goldenMean","goldenMeanRank",
						"recent","recentRank" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<NcaaStats>() {{
				setTargetType(NcaaStats.class);
			}});
		}});
		return reader;
	}



	@Bean
	public ItemWriter<NcaaStats> writer() {
		return new ItemWriter<NcaaStats>() {
			@Override
			public void write(List<? extends NcaaStats> items) throws Exception {
				for(NcaaStats item : items) {
					System.out.println(item);
				}
			}
		};
	}
	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.flow(s1)
				.end()
				.build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<NcaaStats> reader,
					  ItemWriter<NcaaStats> writer) {
		return stepBuilderFactory.get("step1")
				.<NcaaStats, NcaaStats> chunk(10)
				.reader(reader)
				.writer(writer)
				.build();
	}
	// end::jobstep[]


}
