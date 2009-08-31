package com.hospitalbugs.model;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Test;

import com.madgag.testsupport.matchers.IsMap;


public class HospitalInfectionDonorOccupancyTest {

	@Test
	public void shouldReportInfectionSourceForTimeInfectiousPatientIsOnWard() {
		Ward ward = new WardBuilder().toWard();
		Interval infectiousInterval = new Interval(100,107);
		Patient patient = new PatientBuilder().wardStay(ward, infectiousInterval).toPatient();
		Infection infection = new InfectionBuilder().patient(patient).infectious(infectiousInterval).toInfection();
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(asList(infection));
		
		Map<Infection, Duration> infectionSources = donorOccupancy.infectionHistoryFor(ward).infectionSourcesFor(infectiousInterval);
		
		assertThat(infectionSources, IsMap.containingOnly(infection, infectiousInterval.toDuration()));
	}
	
}
