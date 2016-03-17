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

import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author Michael Minella
 */
public class ExponentialDistributionModelTraversalCallbackTests {

	@Test
	public void test() {
		ExponentialDistributionModelTraversalCallback.initialize();
		Map<String, List<Boolean>> odds = ExponentialDistributionModelTraversalCallback.odds;

		for (Map.Entry<String, List<Boolean>> entry : odds.entrySet()) {
			List<Boolean> value = entry.getValue();

			int count = 0;

			for (Boolean aBoolean : value) {
				if(aBoolean) {
					count++;
				}
				else {
					break;
				}
			}

			System.out.println(entry.getKey() + " : " + count + "/" + value.size());
		}
	}
}
