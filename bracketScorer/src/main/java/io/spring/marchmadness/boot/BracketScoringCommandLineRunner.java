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

import javax.sql.DataSource;

import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.BracketRepository;
import io.spring.marchmadness.enricher.BracketScoringTraversalCallback;
import io.spring.marchmadness.enricher.BracketScoringTraversalCallbackFactory;
import io.spring.marchmadness.enricher.TeamPopulatorTraversalCallback;
import io.spring.marchmadness.filter.EliteEightFilterTraversalCallback;
import io.spring.marchmadness.filter.FinalFourFilterTraversalCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Michael Minella
 */
@Component
public class BracketScoringCommandLineRunner implements CommandLineRunner {

	@Autowired
	private BracketRepository bracketRepository;

	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;

	@Override
	public void run(String... strings) throws Exception {
		final AtomicInteger counter = new AtomicInteger(0);

		BracketScoringTraversalCallbackFactory bracketScoringTraversalCallbackFactoryBean =
				new BracketScoringTraversalCallbackFactory();

		bracketScoringTraversalCallbackFactoryBean.setDataSource(dataSource);

		Bracket result = bracketRepository.findViableBrackets()
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
				.map(bracket3 -> {
					TeamPopulatorTraversalCallback callback = new TeamPopulatorTraversalCallback(env);
					bracket3.traverse(callback);
					return bracket3;
				})
				.map(bracket4 -> {
					BracketScoringTraversalCallback callback = bracketScoringTraversalCallbackFactoryBean.getObject();
					bracket4.traverse(callback);
					bracket4.setScore(callback.getBracketScore());
					return bracket4;
				})
				.max((b1, b2) ->
					Long.compare(b1.getScore(), b2.getScore())
				)
				.get();

		System.out.println("************** RESULT ****************");
		System.out.println("Winner: " + result.getWinner().getTeamName());
		System.out.println("Score: " + result.getScore());
		System.out.println("Bracket Id: " + result.getId());

//				.collect(groupingBy(bracket4 ->
//					bracket4.getWinner().getTeamName(), counting()
//				));
//				.forEach(bracket -> {
//					System.out.println("Winner: " + bracket.getWinner().getTeamName() + " " + counter.incrementAndGet());
//		});
//
//		System.out.println("***************** RESULTS *********************");
//		for (Map.Entry<String, Long> winner : results.entrySet()) {
//			System.out.println(winner.getKey() + " won " + winner.getValue() + " times.");
//		}
	}
}
