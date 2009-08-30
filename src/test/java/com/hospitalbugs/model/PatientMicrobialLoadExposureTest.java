package com.hospitalbugs.model;

import java.util.Map;

import com.google.common.collect.SortedSetMultimap;


public class PatientMicrobialLoadExposureTest {

	public void shouldAddToInterval() throws Exception {
		PatientMicrobialLoadExposure calculator = new PatientMicrobialLoadExposure();
		
		Infection infection = new InfectionBuilder().receptionStart;
		
		
		
		
		
		Map<Infection, Float> possibleSources = calculator.possibleSourcesFor(infection);
		
		
		
		
	}
}
