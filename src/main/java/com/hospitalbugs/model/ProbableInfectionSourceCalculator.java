package com.hospitalbugs.model;

import java.util.Map;

import org.joda.time.Interval;

public class ProbableInfectionSourceCalculator {

	public StandardisedMicrobialLoad getStandardisedMicrobialLoadsForEachPotentialInfectingStrainFor(Infection infection) {
		
		Interval susceptibilityInterval = infection.getSusceptibility();
		Patient patient;
		
		Map<Interval,Ward> patientWardOccupanciesDuringSusceptibility = null;
		
		InfectionRiskCalculator infectionRiskCalculator;
		
		StandardisedMicrobialLoad standardisedMicrobialLoad = StandardisedMicrobialLoad.ZERO;
		for (Map.Entry<Interval,Ward> patientWardOccupation : patientWardOccupanciesDuringSusceptibility.entrySet()) {
			Interval wardOccupationInterval = patientWardOccupation.getKey();
			Ward ward = patientWardOccupation.getValue();
			
			standardisedMicrobialLoad = standardisedMicrobialLoad.add(infectionRiskCalculator.standardisedMicrobialLoadFor(ward,wardOccupationInterval));
		}
		
		return standardisedMicrobialLoad;
	}

}
