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
public class Node {

	private Node[] children;

	private Team team;

	private int level;

	public Node(Team team, int level) {
		this.team = team;
		this.level = level;
	}

	public Node[] getChildren() {
		return this.children;
	}

	public Node getLeft() {
		return this.children == null? null : this.children[0];
	}

	public Node getRight() {
		return this.children == null? null : this.children[1];
	}

	void setLeft(Node left) {
		if(this.children == null) {
			this.children = new Node[2];
		}

		this.children[0] = left;
	}

	void setRight(Node right) {
		if(this.children == null) {
			this.children = new Node[2];
		}
		this.children[1] = right;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}

	public int getLevel() {
		return this.level;
	}
}
