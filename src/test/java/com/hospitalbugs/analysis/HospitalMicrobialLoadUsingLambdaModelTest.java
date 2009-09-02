package com.hospitalbugs.analysis;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.joda.time.Duration.standardDays;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.junit.Test;

import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.InfectionBuilder;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.PatientBuilder;
import com.hospitalbugs.model.Ward;
import com.hospitalbugs.model.WardBuilder;


public class HospitalMicrobialLoadUsingLambdaModelTest {
	float lambda = 0.7f;
	float transport = 0.2f;

	@Test
	public void shouldMonkeyItUp() throws Exception {
		Ward ward = new WardBuilder().toWard();
		Ward otherWard = new WardBuilder().toWard();
		Interval infectiousInterval = new Interval(100,107);
		Patient patient = new PatientBuilder().wardStay(ward, infectiousInterval).toPatient();
		Infection infection = new InfectionBuilder().patient(patient).infectious(infectiousInterval).toInfection();
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(asList(infection));
		
		HospitalMicrobialLoad hospitalMicrobialLoad =
			new HospitalMicrobialLoadCalculationFactory().lambdaModel(donorOccupancy, lambda, transport);
		
		assertThat(hospitalMicrobialLoad.microbialLoadFor(ward, infectiousInterval).forInfection(infection), equalTo(1f));
		Interval dayOfInfection = new Interval(infectiousInterval.getStart(),standardDays(1));
		assertThat(hospitalMicrobialLoad.microbialLoadFor(ward, dayOfInfection).forInfection(infection), equalTo(1f));
		assertThat(hospitalMicrobialLoad.microbialLoadFor(otherWard, dayOfInfection).forInfection(infection), equalTo(transport));
		Interval oneDayAfter = standardDays(1).toIntervalFrom(dayOfInfection.getEnd());
		assertThat(hospitalMicrobialLoad.microbialLoadFor(ward, oneDayAfter).forInfection(infection), equalTo(lambda));
		assertThat(hospitalMicrobialLoad.microbialLoadFor(otherWard, oneDayAfter).forInfection(infection), equalTo(transport*lambda));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldHandleABigBunchOfData() throws Exception {
		Interval totalInterval = new Interval(new Instant(), standardDays(365*3));
		HospitalInfectionDonorOccupancy donorOccupancy = RandomHospitalInfectionDataFactory.generate(totalInterval, 2000, 30);
		Interval donorOccupancyTotalInterval = donorOccupancy.getTotalInterval();
		assertThat(donorOccupancyTotalInterval.toDuration(), greaterThan(standardDays(365)));
		HospitalMicrobialLoad hospitalMicrobialLoad =
			new HospitalMicrobialLoadCalculationFactory().lambdaModel(donorOccupancy, lambda, transport);
		
		System.out.println("ba");
	}
}
