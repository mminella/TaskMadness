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
public class FinalFourFilterTraversalCallback implements TraversalCallback {

	private boolean valid = true;
	private int numberOneSeeds = 0;

	//TODO: Add filter for #1 overall seed
	@Override
	public void execute(Node node) {
		if(node.getLevel() == 4) {
			if(node.getTeam().getSeed() == 1) {
				numberOneSeeds++;

				if(numberOneSeeds > 2) {
					valid = false;
				}
			}
		}
	}

	public boolean isValid() {
		return this.valid;
	}
}
