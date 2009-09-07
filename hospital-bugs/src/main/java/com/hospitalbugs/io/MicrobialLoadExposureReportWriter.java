package com.hospitalbugs.io;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.hospitalbugs.analysis.HospitalMicrobialLoad;
import com.hospitalbugs.analysis.PatientMicrobialLoadExposure;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.StandardisedMicrobialLoad;

public class MicrobialLoadExposureReportWriter {

	private final PatientMicrobialLoadExposure exposure;
	private final Writer writer;

	public MicrobialLoadExposureReportWriter(Writer writer, PatientMicrobialLoadExposure patientMicrobialLoadExposure) {
		this.writer = writer;
		this.exposure = patientMicrobialLoadExposure;
	}

	public void write(List<Infection> infections, HospitalMicrobialLoad hospitalMicrobialLoad) throws IOException {
		Joiner joiner = Joiner.on(",");
		for (Infection recipientInfection : infections) {
			StandardisedMicrobialLoad microbialLoad = 
				exposure.microbialLoadExposureDuringPatientSusceptibilityFor(recipientInfection, hospitalMicrobialLoad);
			for (Map.Entry<Infection, Float> entry: microbialLoad.getLoad().entrySet()) {
				Infection donorInfection = entry.getKey();
				Float loadFromDonorInfection = entry.getValue();
				writer.write(joiner.join(recipientInfection.getId(),donorInfection.getId(), loadFromDonorInfection)+"\n");
			}
		}
	}

}
