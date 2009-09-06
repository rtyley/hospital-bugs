package com.hospitalbugs.fixtures;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;

public class InfectionBuilder {

	private static int defaultInfectionId;
	private String infectionId = newInfectionId();
	private Patient patient = new PatientBuilder().toPatient();
	ReadableInstant susceptibilityStart=new Instant(-1000),  transition =new Instant(-100), endOfInfectiousness=new Instant(-10);
	
	private static String newInfectionId() {
		return "I"+(defaultInfectionId++);
	}
	
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
		return new Infection(infectionId, patient, susceptibilityStart, transition, endOfInfectiousness);
	}

}
