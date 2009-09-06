package com.hospitalbugs.model;

import org.joda.time.Interval;
import org.joda.time.ReadableInstant;
import org.joda.time.format.ISODateTimeFormat;

public class Infection {

	private final Patient patient;
	private final Interval infectiousInterval, susceptibilityInterval;
	private final String infectionId;

	public Infection(String infectionId, Patient patient, ReadableInstant susceptibilityStart, ReadableInstant transition, ReadableInstant endOfInfectiousness) {
		this.infectionId = infectionId;
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

	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+patient+":"+ISODateTimeFormat.date().print(getTransition())+"]";
	}

	public ReadableInstant getTransition() {
		return infectiousInterval.getStart();
	}

	public String getId() {
		return infectionId;
	}
}
