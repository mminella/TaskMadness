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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import io.spring.marchmadness.domain.support.ToStringTraversalCallback;

/**
 * Insert logic: http://stackoverflow.com/questions/32223123/inserting-elements-in-binary-tree-in-java
 * Traversal logic: http://www.geeksforgeeks.org/reverse-level-order-traversal/
 *
 * @author Michael Minella
 */
public class Tree {

	public static List<Team> BRACKET_INITIALIZER = new ArrayList<>(127);

	static {
		for (int i = 0; i < 63; i++) {
			BRACKET_INITIALIZER.add(null);
		}

		BRACKET_INITIALIZER.add(new Team(1, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(16, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(8, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(9, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(5, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(12, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(4, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(13, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(6, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(11, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(3, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(14, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(7, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(10, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(2, Region.EAST));
		BRACKET_INITIALIZER.add(new Team(15, Region.EAST));

		BRACKET_INITIALIZER.add(new Team(1, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(16, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(8, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(9, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(5, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(12, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(4, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(13, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(6, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(11, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(3, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(14, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(7, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(10, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(2, Region.MIDWEST));
		BRACKET_INITIALIZER.add(new Team(15, Region.MIDWEST));

		BRACKET_INITIALIZER.add(new Team(1, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(16, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(8, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(9, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(5, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(12, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(4, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(13, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(6, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(11, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(3, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(14, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(7, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(10, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(2, Region.SOUTH));
		BRACKET_INITIALIZER.add(new Team(15, Region.SOUTH));

		BRACKET_INITIALIZER.add(new Team(1, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(16, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(8, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(9, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(5, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(12, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(4, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(13, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(6, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(11, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(3, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(14, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(7, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(10, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(2, Region.WEST));
		BRACKET_INITIALIZER.add(new Team(15, Region.WEST));
	}

	private Node root;

	public Tree() {}

	public Tree(List<Team> teams) {
		for (Team team : teams) {
			insert(team);
		}
	}

	public Node getRoot() {
		return this.root;
	}

	private void insert(Team team) {
		if (root == null) {
			root = new Node(team, 6);
			return;
		}

		Queue<Node> nodesToProcess = new LinkedList<>();
		nodesToProcess.add(root);

		while (true) {
			Node actualNode = nodesToProcess.poll();

			// Left child has precedence over right one
			if (actualNode.getLeft() == null) {
				actualNode.setLeft(new Node(team, actualNode.getLevel() - 1));
				return;
			}
			if (actualNode.getRight() == null) {
				actualNode.setRight(new Node(team, actualNode.getLevel() - 1));
				return;
			}

			// I have both children set, I will process them later if needed
			nodesToProcess.add(actualNode.getLeft());
			nodesToProcess.add(actualNode.getRight());
		}
	}

	public void reverseLevelOrder(TraversalCallback callback) {
		reverseLevelOrder(this.root, callback);
	}

	/* Given a binary tree, print its nodes in reverse level order */
	private void reverseLevelOrder(Node node, TraversalCallback callback) {
		Stack<Node> S = new Stack();
		Queue<Node> Q = new LinkedList();
		Q.add(node);

		// Do something like normal level order traversal order. Following are the
		// differences with normal level order traversal
		// 1) Instead of printing a node, we push the node to stack
		// 2) Right subtree is visited before left subtree
		while (Q.isEmpty() == false) {

            /* Dequeue node and make it root */
			node = Q.peek();
			Q.remove();
			S.push(node);

            /* Enqueue right child */
			if (node.getRight() != null) {
				Q.add(node.getRight()); // NOTE: RIGHT CHILD IS ENQUEUED BEFORE LEFT
			}
            /* Enqueue left child */
			if (node.getLeft() != null) {
				Q.add(node.getLeft());
			}
		}

		// Now pop all items from stack one by one and print them
		while (S.empty() == false) {
			node = S.peek();

			callback.execute(node);

			S.pop();
		}
	}

	@Override
	public String toString() {
		ToStringTraversalCallback callback = new ToStringTraversalCallback();
		this.reverseLevelOrder(callback);

		return callback.getString();
	}
}
