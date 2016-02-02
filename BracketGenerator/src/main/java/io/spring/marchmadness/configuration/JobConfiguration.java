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
package io.spring.marchmadness.configuration;

import java.util.List;

import io.spring.marchmadness.batch.BracketGeneratingItemReader;
import io.spring.marchmadness.batch.DeDupingItemProcessor;
import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.support.ExponentialDistributionModelTraversalCallback;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Minella
 */
@Configuration
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public BracketGeneratingItemReader itemReader() {
		BracketGeneratingItemReader itemReader = new BracketGeneratingItemReader();

		itemReader.setTraversalCallback(new ExponentialDistributionModelTraversalCallback());
		itemReader.setMaxItemCount(1000000);
		itemReader.setName("bracketGeneratingItemReader");

		return itemReader;
	}

	@Bean
	public ItemWriter<Bracket> itemWriter() {
		return new ItemWriter<Bracket>() {
			private int count = 0;

			@Override
			public void write(List<? extends Bracket> items) throws Exception {
				count++;
				System.out.println("writing chunk " + count);
				for (Bracket item : items) {
//					System.out.println(item);
				}
			}
		};
	}

	@Bean
	public ItemProcessor<Bracket, Bracket> itemProcessor() {
		return new DeDupingItemProcessor();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Bracket, Bracket>chunk(10000)
				.reader(itemReader())
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Job generationJob() {
		return jobBuilderFactory.get("generationJob")
				.start(step1())
				.build();
	}
}
