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
package io.spring.marchmadness.batch;

import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.TraversalCallback;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;

/**
 * @author Michael Minella
 */
public class BracketGeneratingItemReader extends AbstractItemCountingItemStreamItemReader<Bracket> {

	private TraversalCallback traversalCallback;


	@Override
	protected Bracket doRead() throws Exception {
		Bracket bracket = new Bracket();

		bracket.simulate(this.traversalCallback);

		return bracket;
	}

	@Override
	protected void doOpen() throws Exception {

	}

	@Override
	protected void doClose() throws Exception {

	}

	public void setTraversalCallback(TraversalCallback traversalCallback) {
		this.traversalCallback = traversalCallback;
	}
}
