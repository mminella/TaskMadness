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

import org.springframework.data.annotation.Id;

/**
 * Source: http://www.geeksforgeeks.org/reverse-level-order-traversal/
 *
 * @author Michael Minella
 */
public class Bracket {

	@Id
	private String id;

	private Tree bracket;

	private long score = 0;

	public Bracket() {
		this.bracket = new Tree(Tree.BRACKET_INITIALIZER);
	}

	public void traverse(TraversalCallback traversalCallback) {
		this.bracket.reverseLevelOrder(traversalCallback);
	}

	public String getId() {
		return this.id;
	}

	public Team getWinner() {
		return this.bracket.getRoot().getTeam();
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return bracket.toString();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}