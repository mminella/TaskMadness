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
package io.spring.marchmadness.boot;

import io.spring.marchmadness.domain.BracketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Michael Minella
 */
@Component
public class BracketScoringCommandLineRunner implements CommandLineRunner {

	@Autowired
	private BracketRepository bracketRepository;

	@Override
	public void run(String... strings) throws Exception {
		bracketRepository.findAllBy()
		.forEach(System.out::println);
	}
}
