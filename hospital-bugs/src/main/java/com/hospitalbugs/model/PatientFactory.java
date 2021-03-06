package com.hospitalbugs.model;

import java.util.HashMap;
import java.util.Map;


public class PatientFactory {

	Map<String,Patient> patients = new HashMap<String,Patient>();
	
	public Patient get(String patientId) {
		Patient patient = patients.get(patientId);
		if (patient==null) {
			patients.put(patientId, patient=new Patient(patientId));
		}
		return patient;
	}

}
