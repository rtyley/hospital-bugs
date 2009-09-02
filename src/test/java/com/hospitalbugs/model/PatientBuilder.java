package com.hospitalbugs.model;

import org.joda.time.Interval;

public class PatientBuilder {

	private static int defaultPatientId = 0;
	
	Patient patient = new Patient(newPatientId());
	
	public PatientBuilder wardStay(Ward ward, Interval interval) {
		patient.addWardStay(ward, interval);
		return this;
	}

	public Patient toPatient() {
		return patient;
	}

	private static String newPatientId() {
		return "P"+(defaultPatientId++);
	}
}
