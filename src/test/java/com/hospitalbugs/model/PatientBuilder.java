package com.hospitalbugs.model;

import java.util.Map;

import org.joda.time.Interval;

public class PatientBuilder {

	Map<Interval, Ward> wardStays;
	
	public PatientBuilder wardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward);
		return this;
	}

	public Patient toPatient() {
		Patient patient = new Patient();
		patient.setWardStays(wardStays);
		return patient;
	}

}
