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

import java.util.concurrent.atomic.AtomicInteger;

import io.spring.marchmadness.domain.BracketRepository;
import io.spring.marchmadness.filters.EliteEightFilterTraversalCallback;
import io.spring.marchmadness.filters.FinalFourFilterTraversalCallback;

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
		final AtomicInteger counter = new AtomicInteger(0);

		bracketRepository.findViableBrackets()
				.filter(bracket1 -> {
					FinalFourFilterTraversalCallback callback = new FinalFourFilterTraversalCallback();
					bracket1.traverse(callback);
					return callback.isValid();
				})
				.filter(bracket2 -> {
					EliteEightFilterTraversalCallback callback = new EliteEightFilterTraversalCallback();
					bracket2.traverse(callback);
					return callback.isValid();
				})
				.forEach(bracket -> {
					System.out.println(bracket.getId() + " " + counter.incrementAndGet());
		});
	}
}
