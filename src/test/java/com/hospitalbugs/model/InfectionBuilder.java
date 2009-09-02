package com.hospitalbugs.model;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

public class InfectionBuilder {

	ReadableInstant susceptibilityStart,  transition , endOfInfectiousness;
	private Patient patient;
	
	public InfectionBuilder infectious(Interval infectiousInterval) {
		return susceptabilityAndInfectiousness(new Duration(1),  infectiousInterval);
	}
	
	public InfectionBuilder susceptabilityAndInfectiousness(Duration susceptabilityDuration, Interval infectiousInterval) {
		susceptibilityStart = infectiousInterval.getStart().minus(susceptabilityDuration);
		transition = infectiousInterval.getStart();
		endOfInfectiousness = infectiousInterval.getEnd();
		return this;
	}

	public InfectionBuilder patient(Patient patient) {
		this.patient = patient;
		return this;
	}

	public Infection toInfection() {
		return new Infection(patient, susceptibilityStart, transition, endOfInfectiousness);
	}

	
	

}
