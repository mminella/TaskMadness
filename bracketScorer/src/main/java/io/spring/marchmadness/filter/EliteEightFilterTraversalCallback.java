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
package io.spring.marchmadness.filter;

import io.spring.marchmadness.domain.Node;
import io.spring.marchmadness.domain.TraversalCallback;

/**
 * @author Michael Minella
 */
public class EliteEightFilterTraversalCallback implements TraversalCallback {

	private boolean valid = false;
	private int goodLowerSeeds = 0;

	@Override
	public void execute(Node node) {
		if(node.getLevel() == 3) {
			int seed = node.getTeam().getSeed();

			if((seed > 2 && seed <= 7) || seed == 10) {
				goodLowerSeeds++;
				valid = true;
			}
		}
	}

	public boolean isValid() {
		return this.valid;
	}
}
