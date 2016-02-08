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
package io.spring.marchmadness.domain;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Michael Minella
 */
public interface BracketRepository extends MongoRepository<Bracket, String> {

	@Query("{$and: [{'bracket.root.children.0.children.0.children.0.children.0.children.0.team.seed': 1}," +
					"{'bracket.root.children.0.children.1.children.0.children.0.children.0.team.seed': 1}," +
					"{'bracket.root.children.1.children.0.children.0.children.0.children.0.team.seed': 1}," +
					"{'bracket.root.children.1.children.1.children.0.children.0.children.0.team.seed': 1}," +
					"{$or: [{'bracket.root.children.0.children.0.team.seed': 1}," +
					"               {'bracket.root.children.0.children.1.team.seed': 1}," +
					"               {'bracket.root.children.1.children.0.team.seed': 1}," +
					"               {'bracket.root.children.1.children.1.team.seed': 1}" +
					"      ]}," +
					"{$or: [{'bracket.root.children.0.children.0.children.0.children.0.children.1.team.seed': 9}," +
					"               {'bracket.root.children.0.children.1.children.0.children.0.children.1.team.seed': 9}," +
					"               {'bracket.root.children.1.children.0.children.0.children.0.children.1.team.seed': 9}," +
					"               {'bracket.root.children.1.children.1.children.0.children.0.children.1.team.seed': 9}" +
					"      ]}," +
					"{$or: [{'bracket.root.children.0.children.0.children.0.children.1.children.0.team.seed': 12}," +
					"   {'bracket.root.children.0.children.1.children.0.children.1.children.0.team.seed': 12}," +
					"   {'bracket.root.children.1.children.0.children.0.children.1.children.0.team.seed': 12}," +
					"   {'bracket.root.children.1.children.1.children.0.children.1.children.0.team.seed': 12}" +
					"      ]}," +
					"{$or: [{'bracket.root.children.0.children.0.children.0.children.1.children.1.team.seed': 13}," +
					"   {'bracket.root.children.0.children.0.children.1.children.0.children.1.team.seed': 14}," +
					"   {'bracket.root.children.0.children.0.children.1.children.1.children.1.team.seed': 15}," +
					"   {'bracket.root.children.0.children.1.children.0.children.1.children.1.team.seed': 13}," +
					"   {'bracket.root.children.0.children.1.children.1.children.0.children.1.team.seed': 14}," +
					"   {'bracket.root.children.0.children.1.children.1.children.1.children.1.team.seed': 15}," +
					"   {'bracket.root.children.1.children.0.children.0.children.1.children.1.team.seed': 13}," +
					"   {'bracket.root.children.1.children.0.children.1.children.0.children.1.team.seed': 14}," +
					"   {'bracket.root.children.1.children.0.children.1.children.1.children.1.team.seed': 15}," +
					"   {'bracket.root.children.1.children.1.children.0.children.1.children.1.team.seed': 13}," +
					"   {'bracket.root.children.1.children.1.children.1.children.0.children.1.team.seed': 14}," +
					"   {'bracket.root.children.1.children.1.children.1.children.1.children.1.team.seed': 15}" +
					"      ]}," +
					"{$and: [{'bracket.root.children.0.children.0.children.0.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.0.children.0.children.1.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.0.children.1.children.0.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.0.children.1.children.1.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.1.children.0.children.0.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.1.children.0.children.1.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.1.children.1.children.0.team.seed': {$ne : 7}}," +
					"   {'bracket.root.children.1.children.1.children.1.team.seed': {$ne : 7}}" +
					"   ]}" +
					"]}")
	Stream<Bracket> findViableBrackets();
}
