package com.hospitalbugs.analysis;

import java.util.Map;

import com.google.common.collect.SortedSetMultimap;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.PatientMicrobialLoadExposure;
import com.hospitalbugs.analysis.PatientMicrobialLoadExposure;


public class PatientMicrobialLoadExposureTest {

	public void shouldAddToInterval() throws Exception {
		PatientMicrobialLoadExposure calculator = new PatientMicrobialLoadExposure();
		
		Infection infection = new InfectionBuilder().receptionStart;
		
		
		
		
		
		Map<Infection, Float> possibleSources = calculator.possibleSourcesFor(infection);
		
		
		
		
	}
}
