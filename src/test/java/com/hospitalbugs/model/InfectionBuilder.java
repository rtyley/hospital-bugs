package com.hospitalbugs.model;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

public class InfectionBuilder {

	ReadableInstant susceptibilityStart=new Instant(-1000),  transition =new Instant(-100), endOfInfectiousness=new Instant(-10);
	private Patient patient = new PatientBuilder().toPatient();
	
	public InfectionBuilder infectious(Interval infectiousInterval) {
		return susceptabilityAndInfectiousness(new Duration(1),  infectiousInterval);
	}
	
	public InfectionBuilder susceptability(Interval susceptabilityInterval) {
		return susceptabilityAndInfectiousness(susceptabilityInterval,new Duration(1));
	}
	
	public InfectionBuilder susceptabilityAndInfectiousness(Duration susceptabilityDuration, Interval infectiousInterval) {
		susceptibilityStart = infectiousInterval.getStart().minus(susceptabilityDuration);
		transition = infectiousInterval.getStart();
		endOfInfectiousness = infectiousInterval.getEnd();
		return this;
	}
	
	public InfectionBuilder susceptabilityAndInfectiousness(Interval susceptabilityInterval, Duration infectiousDuration) {
		susceptibilityStart = susceptabilityInterval.getStart();
		transition = susceptabilityInterval.getEnd();
		endOfInfectiousness = susceptabilityInterval.getEnd().plus(infectiousDuration);
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
