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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
	public static final Map<String, List<Boolean>> odds = new TreeMap<>();

	static {
		initialize();
	}

	protected static void initialize() {
		probabilities.put(1, Arrays.asList(generateArray(124.0/124.0), generateArray(107.0/124.0), generateArray(85.0/124.0), generateArray(51.0/124.0), generateArray(29.0/62.0), generateArray(19.0/31.0)));
		probabilities.put(2, Arrays.asList(generateArray(117.0/124.0), generateArray(79.0/124.0), generateArray(58.0/124.0), generateArray(26.0/124.0), generateArray(12.0/62.0), generateArray(4.0/31.0)));
		probabilities.put(3, Arrays.asList(generateArray(104.0/124.0), generateArray(63.0/124.0), generateArray(31.0/124.0), generateArray(14.0/124.0), generateArray(9.0/62.0), generateArray(4.0/31.0)));
		probabilities.put(4, Arrays.asList(generateArray(99.0/124.0), generateArray(57.0/124.0), generateArray(20.0/124.0), generateArray(13.0/124.0), generateArray(3.0/62.0), generateArray(1.0/31.0)));
		probabilities.put(5, Arrays.asList(generateArray(80.0/124.0), generateArray(41.0/124.0), generateArray(8.0/124.0), generateArray(6.0/124.0), generateArray(3.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(6, Arrays.asList(generateArray(81.0/124.0), generateArray(41.0/124.0), generateArray(13.0/124.0), generateArray(3.0/124.0), generateArray(2.0/62.0), generateArray(1.0/31.0)));
		probabilities.put(7, Arrays.asList(generateArray(76.0/124.0), generateArray(22.0/124.0), generateArray(9.0/124.0), generateArray(2.0/124.0), generateArray(1.0/62.0), generateArray(1.0/31.0)));
		probabilities.put(8, Arrays.asList(generateArray(63.0/124.0), generateArray(12.0/124.0), generateArray(8.0/124.0), generateArray(5.0/124.0), generateArray(3.0/62.0), generateArray(1.0/31.0)));
		probabilities.put(9, Arrays.asList(generateArray(61.0/124.0), generateArray(5.0/124.0), generateArray(2.0/124.0), generateArray(1.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(10, Arrays.asList(generateArray(48.0/124.0), generateArray(22.0/124.0), generateArray(7.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(11, Arrays.asList(generateArray(43.0/124.0), generateArray(18.0/124.0), generateArray(6.0/124.0), generateArray(3.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(12, Arrays.asList(generateArray(44.0/124.0), generateArray(20.0/124.0), generateArray(1.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(13, Arrays.asList(generateArray(25.0/124.0), generateArray(6.0/124.0), generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(14, Arrays.asList(generateArray(20.0/124.0), generateArray(2.0/124.0), generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(15, Arrays.asList(generateArray(7.0/124.0), generateArray(1.0/124.0), generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));
		probabilities.put(16, Arrays.asList(generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/124.0), generateArray(0.0/62.0), generateArray(0.0/31.0)));

		int i = 0;
		for(; i < 6; i++) {
			int j = 1;
			for(; j < 17; j++) {
				int k = 1;
				for(; k < 17; k++) {
					List<Boolean> temp = new LinkedList<>();

					boolean[] firstSeed = probabilities.get(j).get(i);
					boolean[] secondSeed = probabilities.get(k).get(i);

					String key;

					if(j < k) {
						key = i + ":" + j + "-" + k;
						for (boolean b : firstSeed) {
							if(b) {
								temp.add(true);
							}
							else {
								break;
							}
						}

						for (boolean b : secondSeed) {
							if(b) {
								temp.add(false);
							}
							else {
								break;
							}
						}
					}
					else if(j > k) {
						key = i + ":" + k + "-" + j;

						for (boolean b : secondSeed) {
							if(b) {
								temp.add(true);
							}
							else {
								break;
							}
						}

						for (boolean b : firstSeed) {
							if(b) {
								temp.add(false);
							}
							else {
								break;
							}
						}
					}
					else {
						key = i + ":" + k + "-" + k;

						for (boolean b : firstSeed) {
							if(b) {
								temp.add(true);
							}
							else {
								break;
							}
						}

						for (boolean b : secondSeed) {
							if(b) {
								temp.add(false);
							}
							else {
								break;
							}
						}
					}

					if(temp.isEmpty()) {
						temp.add(true);
						temp.add(false);
					}

					odds.put(key, temp);
				}
			}
		}
	}

	private static boolean[] generateArray(double rate) {
		double wins = rate * 10000;

		boolean[] outcomes = new boolean[10000];

		int i = 0;

		for(; i < wins; i++) {
			outcomes[i] = true;
		}

		for(; i < 10000; i++) {
			outcomes[i] = false;
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

			String key = (node.getLevel() - 1) + ":" + lowSeed.getSeed() + "-" + highSeed.getSeed();

			List<Boolean> outcomes = odds.get(key);

			ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

			System.out.println("key = " + key + " size = " + ((outcomes != null) ? outcomes.size() : "doesn't exist"));

			int index = randomGenerator.nextInt(outcomes.size());

			if(outcomes.get(index)) {
				node.setTeam(lowSeed);
			}
			else {
				node.setTeam(highSeed);
			}
		}
	}
}
