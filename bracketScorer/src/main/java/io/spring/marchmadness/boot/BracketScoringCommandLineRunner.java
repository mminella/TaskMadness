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

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.BracketRepository;
import io.spring.marchmadness.enricher.BracketScoringTraversalCallback;
import io.spring.marchmadness.enricher.BracketScoringTraversalCallbackFactory;
import io.spring.marchmadness.domain.support.TeamPopulatorTraversalCallback;
import io.spring.marchmadness.filter.EliteEightFilterTraversalCallback;
import io.spring.marchmadness.filter.FinalFourFilterTraversalCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Michael Minella
 */
@Component
public class BracketScoringCommandLineRunner implements CommandLineRunner {

	public static final String MIN_BRACKET = "minBracket";

	public static final String MAX_BRACKET = "maxBracket";

	private static final String BRACKET_RESULTS = "insert into BRACKET_RESULTS (ID, SCORE) values (?, ?)";

	@Autowired
	private BracketRepository bracketRepository;

	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;

	@Override
	public void run(String... strings) throws Exception {

		BracketScoringTraversalCallbackFactory bracketScoringTraversalCallbackFactoryBean =
				new BracketScoringTraversalCallbackFactory();

		bracketScoringTraversalCallbackFactoryBean.setDataSource(dataSource);

		Map<String, Bracket> results = new HashMap<>(2);

		bracketRepository.findViableBrackets()
				.filter(bracket -> {
					FinalFourFilterTraversalCallback callback = new FinalFourFilterTraversalCallback();
					bracket.traverse(callback);
					return callback.isValid();
				})
				.filter(bracket -> {
					EliteEightFilterTraversalCallback callback = new EliteEightFilterTraversalCallback();
					bracket.traverse(callback);
					return callback.isValid();
				})
				.map(bracket -> {
					TeamPopulatorTraversalCallback callback = new TeamPopulatorTraversalCallback(env);
					bracket.traverse(callback);
					return bracket;
				})
				.map(bracket -> {
					BracketScoringTraversalCallback callback = bracketScoringTraversalCallbackFactoryBean.getObject();
					bracket.traverse(callback);
					bracket.setScore(callback.getBracketScore());
					return bracket;
				})
				.forEach(bracket -> {
					if(!results.containsKey(MIN_BRACKET)) {
						results.put(MIN_BRACKET, bracket);
						results.put(MAX_BRACKET, bracket);
					}
					else {
						Bracket minBracket = results.get(MIN_BRACKET);
						Bracket maxBracket = results.get(MAX_BRACKET);

						if(minBracket.getScore() > bracket.getScore()) {
							results.put(MIN_BRACKET, bracket);
						}

						if(maxBracket.getScore() < bracket.getScore()) {
							results.put(MAX_BRACKET, bracket);
						}
					}
				});

		JdbcOperations jdbcTemplate = new JdbcTemplate(this.dataSource);

		Bracket maxBracket = results.get(MAX_BRACKET);

		jdbcTemplate.update(BRACKET_RESULTS, maxBracket.getId(), maxBracket.getScore());

		System.out.println("************** MAX ****************");
		System.out.println("Winner: " + maxBracket.getWinner().getTeamName());
		System.out.println("Score: " + maxBracket.getScore());
		System.out.println("Bracket Id: " + maxBracket.getId());

		Bracket minBracket = results.get(MIN_BRACKET);

		jdbcTemplate.update(BRACKET_RESULTS, minBracket.getId(), minBracket.getScore());

		System.out.println("************** MIN ****************");
		System.out.println("Winner: " + minBracket.getWinner().getTeamName());
		System.out.println("Score: " + minBracket.getScore());
		System.out.println("Bracket Id: " + minBracket.getId());

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
