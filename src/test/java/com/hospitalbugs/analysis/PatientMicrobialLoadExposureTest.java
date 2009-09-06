package com.hospitalbugs.analysis;

import static com.hospitalbugs.model.StandardisedMicrobialLoad.loadOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.joda.time.Interval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hospitalbugs.fixtures.InfectionBuilder;
import com.hospitalbugs.fixtures.PatientBuilder;
import com.hospitalbugs.fixtures.WardBuilder;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;

@RunWith(MockitoJUnitRunner.class)
public class PatientMicrobialLoadExposureTest {

	@Mock HospitalMicrobialLoad loadModel;
	PatientMicrobialLoadExposure exposure = new PatientMicrobialLoadExposure();
	Ward ward = new WardBuilder().toWard();
	Infection donorInfection = new InfectionBuilder().toInfection();
	Infection nonRelevantInfection = new InfectionBuilder().toInfection();
	
	@Test
	public void shouldNotIncludePortionOfWardStayAfterEndOfSusceptibilityInMicrobialLoadExposure() {
		Patient patientWhoStaysInWardAfterTransition = new PatientBuilder().wardStay(ward, new Interval( 0, 1000)).toPatient();
		Infection targetInfection = new InfectionBuilder().patient(patientWhoStaysInWardAfterTransition).susceptability(new Interval(333,555)).toInfection();
		
		StandardisedMicrobialLoad expectedLoadForExposure = loadOf(donorInfection, 0.8f);
		when(loadModel.microbialLoadFor(ward, targetInfection.getSusceptibility())).thenReturn(expectedLoadForExposure);
		when(loadModel.microbialLoadFor(eq(ward), argThat(is(not(targetInfection.getSusceptibility()))))).thenReturn(loadOf(nonRelevantInfection, 0.6f));
		
		StandardisedMicrobialLoad microbialLoadExposure =
			exposure.microbialLoadExposureDuringPatientSusceptibilityFor(targetInfection, loadModel);
		
		verify(loadModel).microbialLoadFor(ward, targetInfection.getSusceptibility());
		verifyNoMoreInteractions(loadModel);
		assertThat(microbialLoadExposure, equalTo(expectedLoadForExposure));
	}
	
	@Test
	public void shouldExcludeTargetInfectionAsPossibleDonor() {
		Patient patient = new PatientBuilder().wardStay(ward, new Interval( 0, 500)).toPatient();
		Infection targetInfection = new InfectionBuilder().patient(patient).susceptability(new Interval(0,400)).toInfection();
		
		StandardisedMicrobialLoad loadCalculatedByCourseGrainedModel = loadOf(targetInfection, 0.8f);
		when(loadModel.microbialLoadFor(eq(ward), any(Interval.class))).thenReturn(loadCalculatedByCourseGrainedModel);
		
		StandardisedMicrobialLoad microbialLoadExposure =
			exposure.microbialLoadExposureDuringPatientSusceptibilityFor(targetInfection, loadModel);
		
		assertThat(microbialLoadExposure.forInfection(targetInfection), equalTo(0f));
		
	}
	
}
