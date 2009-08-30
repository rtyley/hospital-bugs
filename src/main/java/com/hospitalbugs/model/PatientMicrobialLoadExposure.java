package com.hospitalbugs.model;

import static com.hospitalbugs.model.StandardisedMicrobialLoad.ZERO;

import java.util.Map;

import org.joda.time.Interval;

public class PatientMicrobialLoadExposure {

	public StandardisedMicrobialLoad microbialLoadExposureDuringPatientSusceptibilityFor(
			Infection infection,
			HospitalMicrobialLoad hospitalMicrobialLoad) {
		
		Interval susceptibilityInterval = infection.getSusceptibility();
		Patient patient = infection.getPatient();
		
		Map<Interval,Ward> patientWardOccupanciesDuringSusceptibility = patient.getWardOccupanciesDuring(susceptibilityInterval);
		
		StandardisedMicrobialLoad microbialLoad = ZERO;
		for (Map.Entry<Interval,Ward> patientWardOccupation : patientWardOccupanciesDuringSusceptibility.entrySet()) {
			Ward ward = patientWardOccupation.getValue();
			Interval wardOccupationInterval = patientWardOccupation.getKey();
			
			microbialLoad = microbialLoad.add(hospitalMicrobialLoad.microbialLoadFor(ward,wardOccupationInterval));
		}
		
		return microbialLoad;
	}

}
