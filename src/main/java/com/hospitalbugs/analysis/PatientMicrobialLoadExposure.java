package com.hospitalbugs.analysis;

import static com.hospitalbugs.model.StandardisedMicrobialLoad.ZERO;

import java.util.Map;

import org.joda.time.Interval;

import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;

public class PatientMicrobialLoadExposure {

	public StandardisedMicrobialLoad microbialLoadExposureDuringPatientSusceptibilityFor(
			Infection infection,
			HospitalMicrobialLoad hospitalMicrobialLoad) {
		
		Interval susceptibilityInterval = infection.getSusceptibility();
		Patient patient = infection.getPatient();
		
		Map<Interval,Ward> patientWardOccupanciesDuringSusceptibility = patient.getWardsOccupiedDuring(susceptibilityInterval);
		
		StandardisedMicrobialLoad microbialLoad = ZERO;
		for (Map.Entry<Interval,Ward> patientWardOccupation : patientWardOccupanciesDuringSusceptibility.entrySet()) {
			Ward ward = patientWardOccupation.getValue();
			Interval wardOccupationDuringSusceptibility = patientWardOccupation.getKey().overlap(susceptibilityInterval);
			
			microbialLoad = microbialLoad.add(hospitalMicrobialLoad.microbialLoadFor(ward,wardOccupationDuringSusceptibility));
		}
		return microbialLoad.remove(infection);
	}

}
