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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Michael Minella
 */
public class BracketScoringTraversalCallbackFactory {

	private static final String USA_TODAY_STATS = "select name, rating from NCAA_STATS where year = 2016 order by rating desc";

	private JdbcOperations jdbcTemplate;

	private final Map<String, Long> rankings = new HashMap<>();

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public BracketScoringTraversalCallback getObject() {

		if(rankings.isEmpty()) {
			jdbcTemplate.query(USA_TODAY_STATS, new RowMapper<Object>() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					rankings.put(rs.getString("name"), rs.getLong("rating"));
					return null;
				}
			});
		}

		BracketScoringTraversalCallback callback = new BracketScoringTraversalCallback(rankings);

		return callback;
	}
}
