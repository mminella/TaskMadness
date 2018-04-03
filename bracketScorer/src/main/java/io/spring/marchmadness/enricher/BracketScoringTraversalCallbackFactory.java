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
package io.spring.marchmadness.enricher;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Michael Minella
 */
public class BracketScoringTraversalCallbackFactory {

	private static final String USA_TODAY_STATS = "select name, rating from NCAA_STATS where year = 2018 order by rating desc";
	private static final String MOORE_STATS = "select name, pr from MOORE_NCAA_STATS where year = 2018 order by pr desc";
	private static final String KENPOM_STATS = "select name, rank from KENPOM_STATS where year = 2018 order by rank desc";

	private JdbcOperations jdbcTemplate;

	private final Map<String, Long> rankings = new HashMap<>();

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public BracketScoringTraversalCallback getObject() {

		final Map<String, Long> usaTodayRankings = new HashMap<>();
		final Map<String, Long> mooreRankings = new HashMap<>();
		final Map<String, Long> kenpomRankings = new HashMap<>();

		if(rankings.isEmpty()) {
			jdbcTemplate.query(USA_TODAY_STATS, (rs, rowNum) -> {
				usaTodayRankings.put(rs.getString("name"), rs.getLong("rating"));
				return null;
			});
			jdbcTemplate.query(MOORE_STATS, (rs, rowNum) -> {
				mooreRankings.put(rs.getString("name"), rs.getLong("pr"));
				return null;
			});
			jdbcTemplate.query(KENPOM_STATS, (rs, rowNum) -> {
				kenpomRankings.put(rs.getString("name"), rs.getLong("rank"));
				return null;
			});

			System.out.println(">> entryset = " + usaTodayRankings.size());
			for (Map.Entry<String, Long> teamRanking : usaTodayRankings.entrySet()) {

				String teamName = teamRanking.getKey();

				System.out.println(">> Calculating rankings for team: " + teamName);

				if(mooreRankings.containsKey(teamName) && kenpomRankings.containsKey(teamName)) {
					rankings.put(teamName, (teamRanking.getValue() * 3) + (mooreRankings.get(teamName) * 3) + (kenpomRankings.get(teamName) * 6));
				}

				if(!mooreRankings.containsKey(teamName)) {
					System.out.println("Moore missing name: " + teamName);
				}

				if(!kenpomRankings.containsKey(teamName)) {
					System.out.println("Kenpom missing name: " + teamName);
				}
			}
		}

		BracketScoringTraversalCallback callback = new BracketScoringTraversalCallback(rankings);

		return callback;
	}
}
