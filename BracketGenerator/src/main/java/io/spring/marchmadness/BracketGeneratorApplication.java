package io.spring.marchmadness;

import io.spring.marchmadness.domain.Bracket;
import io.spring.marchmadness.domain.support.ExponentialDistributionModelTraversalCallback;

//@SpringBootApplication
public class BracketGeneratorApplication {

	public static void main(String[] args) {
		Bracket bracket = new Bracket();

		bracket.simulate(new ExponentialDistributionModelTraversalCallback());

		System.out.println(bracket);
	}
}
