package com.hospitalbugs.model;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class InfectionBuilder {

	Instant susceptibilityStart,  transition , endOfInfectiousness;
	private Patient patient;
	
	public InfectionBuilder infectious(Interval infectiousInterval) {
		transition = infectiousInterval.getStart().toInstant();
		endOfInfectiousness = infectiousInterval.getEnd().toInstant();
		if (susceptibilityStart==null) {
			susceptibilityStart = transition.minus(1);
		}
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
