package com.hospitalbugs.fixtures;

import org.joda.time.Interval;

import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.Ward;

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
