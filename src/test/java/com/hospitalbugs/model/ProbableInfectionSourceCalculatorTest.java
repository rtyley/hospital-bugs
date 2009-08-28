package com.hospitalbugs.model;

import java.util.Map;

import com.google.common.collect.SortedSetMultimap;


public class ProbableInfectionSourceCalculatorTest {

	public void shouldAddToInterval() throws Exception {
		ProbableInfectionSourceCalculator calculator = new ProbableInfectionSourceCalculator();
		
		Infection infection = new InfectionBuilder().receptionStart;
		
		
		
		
		
		Map<Infection, Float> possibleSources = calculator.possibleSourcesFor(infection);
		
		
		
		
	}
}
