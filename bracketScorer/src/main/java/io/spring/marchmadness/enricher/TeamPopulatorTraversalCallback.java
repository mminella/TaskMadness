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

import io.spring.marchmadness.domain.Node;
import io.spring.marchmadness.domain.Team;
import io.spring.marchmadness.domain.TraversalCallback;

import org.springframework.core.env.Environment;

/**
 * @author Michael Minella
 */
public class TeamPopulatorTraversalCallback implements TraversalCallback {

	private Environment env;

	public TeamPopulatorTraversalCallback(Environment env) {
		this.env = env;
	}

	@Override
	public void execute(Node node) {
		Team team = node.getTeam();
		team.setTeamName(env.getProperty(team.getRegion() + "." + team.getSeed()));
	}
}
