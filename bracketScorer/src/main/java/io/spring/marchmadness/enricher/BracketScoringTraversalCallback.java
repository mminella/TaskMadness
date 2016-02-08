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

import java.util.Map;

import io.spring.marchmadness.domain.Node;
import io.spring.marchmadness.domain.TraversalCallback;

/**
 * @author Michael Minella
 */
public class BracketScoringTraversalCallback implements TraversalCallback {

	private Map<String, Long> rankings;
	private long bracketScore = 0;

	public BracketScoringTraversalCallback(Map<String, Long> rankings) {
		this.rankings = rankings;
	}

	@Override
	public void execute(Node node) {
		if(isLeafNode(node)) {
			long leftRanking = rankings.get(node.getLeft().getTeam().getTeamName());
			long rightRanking = rankings.get(node.getRight().getTeam().getTeamName());

			if(leftRanking > rightRanking && node.getTeam().getTeamName().equalsIgnoreCase(node.getLeft().getTeam().getTeamName())) {
				bracketScore += score(node);
			}
			else if(rightRanking > leftRanking && node.getTeam().getTeamName().equalsIgnoreCase(node.getLeft().getTeam().getTeamName())){
				bracketScore += score(node);
			}
		}
	}

	public long getBracketScore() {
		return bracketScore;
	}

	private long score(Node node) {
		int level = node.getLevel();

		return (level^2) * 10;
	}
}
