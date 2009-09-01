package com.hospitalbugs.model;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class Infection {

	private final Patient patient;
	private final Interval infectiousInterval, susceptibilityInterval;

	public Infection(Patient patient, Instant susceptibilityStart, Instant transition, Instant endOfInfectiousness) {
		this.patient = patient;
		if (susceptibilityStart.isAfter(transition)) {
			throw new IllegalArgumentException();
		}
		if (transition.isAfter(endOfInfectiousness)) {
			throw new IllegalArgumentException();
		}
		susceptibilityInterval = new Interval(susceptibilityStart, transition);
		infectiousInterval = new Interval(transition, endOfInfectiousness);
	}

	public Patient getPatient() {
		return patient;
	}

	public Interval getInfectiousInterval() {
		return infectiousInterval;
	}
	
	public Interval getSusceptibility() {
		return susceptibilityInterval;
	}

}
