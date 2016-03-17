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
package io.spring.marchmadness.domain.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import io.spring.marchmadness.domain.Node;
import io.spring.marchmadness.domain.Team;
import io.spring.marchmadness.domain.TraversalCallback;

/**
 * Source: <a href="https://www.youtube.com/watch?v=lo1WYbIvBTA&index=29&list=WL">https://www.youtube.com/watch?v=lo1WYbIvBTA&amp;index=29&amp;list=WL</a>
 *
 * @author Michael Minella
 */
public class ExponentialDistributionModelTraversalCallback implements TraversalCallback {

	private static final Map<Integer, List<boolean[]>> probabilities = new HashMap<>(16);

	static {
		probabilities.put(1, Arrays.asList(generateArray(4.0), generateArray(3.48), generateArray(2.76), generateArray(1.62), generateArray(.93), generateArray(.62)));
		probabilities.put(2, Arrays.asList(generateArray(3.76), generateArray(2.59), generateArray(1.86), generateArray(.86), generateArray(.41), generateArray(.14)));
		probabilities.put(3, Arrays.asList(generateArray(3.41), generateArray(2.07), generateArray(1.03), generateArray(.48), generateArray(.31), generateArray(.14)));
		probabilities.put(4, Arrays.asList(generateArray(3.14), generateArray(1.76), generateArray(.62), generateArray(.45), generateArray(.1), generateArray(.03)));
		probabilities.put(5, Arrays.asList(generateArray(2.59), generateArray(1.34), generateArray(.28), generateArray(.21), generateArray(.1), generateArray(0.0)));
		probabilities.put(6, Arrays.asList(generateArray(2.66), generateArray(1.34), generateArray(.45), generateArray(.1), generateArray(.07), generateArray(.03)));
		probabilities.put(7, Arrays.asList(generateArray(2.41), generateArray(.66), generateArray(.24), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(8, Arrays.asList(generateArray(1.93), generateArray(.34), generateArray(.24), generateArray(.14), generateArray(.07), generateArray(.03)));
		probabilities.put(9, Arrays.asList(generateArray(2.07), generateArray(.17), generateArray(.07), generateArray(.03), generateArray(0.0), generateArray(0.0)));
		probabilities.put(10, Arrays.asList(generateArray(1.59), generateArray(.72), generateArray(.24), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(11, Arrays.asList(generateArray(1.34), generateArray(.52), generateArray(.17), generateArray(.1), generateArray(0.0), generateArray(0.0)));
		probabilities.put(12, Arrays.asList(generateArray(1.41), generateArray(.69), generateArray(.03), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(13, Arrays.asList(generateArray(.86), generateArray(.21), generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(14, Arrays.asList(generateArray(.59), generateArray(.07), generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(15, Arrays.asList(generateArray(.24), generateArray(.03), generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
		probabilities.put(16, Arrays.asList(generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0), generateArray(0.0)));
	}

	private static boolean[] generateArray(double rate) {
		double wins = (rate * 100 / 400) * 10000;

		boolean[] outcomes = new boolean[10000];

		int i = 0;

		for(; i < wins; i++) {
			outcomes[i] = true;
		}

		for(; i < 10000; i++) {
			outcomes[1] = false;
		}

		return outcomes;
	}

	@Override
	public void execute(Node node) {
		if(node.getLeft() != null && node.getRight() != null) {
			Team left = node.getLeft().getTeam();
			Team right = node.getRight().getTeam();

			Team highSeed, lowSeed;

			if(left.getSeed() > right.getSeed()) {
				highSeed = left;
				lowSeed = right;
			}
			else {
				highSeed = right;
				lowSeed = left;
			}

			List<boolean[]> outcomes = probabilities.get(highSeed.getSeed());

			ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

			int index = randomGenerator.nextInt(10000);

			if(outcomes.get(node.getLevel() - 1)[index]) {
				node.setTeam(highSeed);
			}
			else {
				node.setTeam(lowSeed);
			}
		}
	}
}