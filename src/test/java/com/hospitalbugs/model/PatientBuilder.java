package com.hospitalbugs.model;

import org.joda.time.Interval;

public class PatientBuilder {

	Patient patient = new Patient();
	
	public PatientBuilder wardStay(Ward ward, Interval interval) {
		patient.addWardStay(ward, interval);
		return this;
	}

	public Patient toPatient() {
		return patient;
	}

}
