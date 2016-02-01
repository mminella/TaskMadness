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

/**
 * Source: http://www.geeksforgeeks.org/reverse-level-order-traversal/
 *
 * @author Michael Minella
 */
public class Bracket {

	private Tree left;
	private Tree right;
	private Node champion;

	public Bracket() {
		this.left = new Tree(Tree.SOUTH_WEST_TEAM_INITIALIZER);
		this.right = new Tree(Tree.EAST_MIDWEST_TEAM_INITIALIZER);
	}

	public void simulate(TraversalCallback traversalCallback) {
		this.left.reverseLevelOrder(traversalCallback);
		this.right.reverseLevelOrder(traversalCallback);

		this.champion = new Node(null, 7);
		this.champion.setLeft(this.left.getRoot());
		this.champion.setRight(this.right.getRoot());

		traversalCallback.execute(this.champion);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();

		string.append(left.toString());
		string.append(this.champion.getTeam());
		string.append(right.toString());

		return string.toString();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}