package com.hospitalbugs.parser;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hospitalbugs.fixtures.PatientBuilder;
import com.hospitalbugs.fixtures.WardBuilder;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.Ward;
import com.madgag.testsupport.matchers.IsMap;

@RunWith(MockitoJUnitRunner.class)
public class PatientWardStayCSVLineParserTest {
	
	@Mock PatientFactory patientFactory;
	@Mock WardFactory wardFactory;
	
	@Test
	public void shouldParseSampleLine() {
		DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/London");

		Patient patient = new PatientBuilder().toPatient();
		Ward ward = new WardBuilder().toWard();
		
		when(patientFactory.get("1234")).thenReturn(patient);
		when(wardFactory.get("7A")).thenReturn(ward);
		
		PatientWardStayCSVLineParser parser = new PatientWardStayCSVLineParser(patientFactory, wardFactory, dateTimeZone);
		
		Patient parsedPatient = parser.parse(asList("1234","7A","2009-04-27T19:00","2009-04-27T19:30"));
		
		assertThat(parsedPatient, sameInstance(patient));
		DateTime start = new DateTime(2009, 4, 27, 19, 00, 0, 0, dateTimeZone);
		DateTime end = new DateTime(2009, 4, 27, 19, 30, 0, 0, dateTimeZone);		
		Interval wardStayInterval = new Interval(start,end);
		assertThat(patient.getWardsOccupiedDuring(wardStayInterval), IsMap.containingOnly(wardStayInterval, ward));
	}
}
