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
package io.spring.marchmadness.controller;

import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.BracketRepository;
import io.spring.marchmadness.domain.support.TeamPopulatorTraversalCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Michael Minella
 */
@Controller
@RequestMapping("/")
public class BracketController {

	private static final String MAX_BRACKET = "select a.id from (select max(score), id from BRACKET_RESULTS) as a";

	@Autowired
	private JdbcOperations jdbcTemplate;

	@Autowired
	private BracketRepository bracketRepository;

	@Autowired
	private Environment env;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String get(Model model) {
		String bracketId = jdbcTemplate.queryForObject(MAX_BRACKET, String.class);

		System.out.println(">> Going to display id " + bracketId);

		model.addAttribute("bracketId", bracketId);

		return "bracket/index";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public Bracket get(@PathVariable("id") String id, Model model) {
		Bracket bracket = bracketRepository.findOne(id);

		bracket.traverse(new TeamPopulatorTraversalCallback(env));

		return bracket;
	}
}
