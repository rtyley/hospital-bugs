package com.hospitalbugs.parser;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;

@RunWith(MockitoJUnitRunner.class)
public class InfectionCSVLineParserTest {
	@Mock PatientFactory patientFactory;
	
	@Test
	public void shouldParseSampleLine() {
		DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/London");

		Patient patient = new PatientBuilder().toPatient();
		
		when(patientFactory.get("1234")).thenReturn(patient);
		
		InfectionCSVLineParser parser = new InfectionCSVLineParser(patientFactory, dateTimeZone);
		
		Infection infection = parser.parse(asList("I1234","1234","2009-04-27T19:00","2009-04-28T09:00","2009-04-30T11:30"));
		
		assertThat(infection.getId(), equalTo("I1234"));
		assertThat(infection.getPatient(), sameInstance(patient));
		DateTime start = new DateTime(2009, 4, 27, 19, 00, 0, 0, dateTimeZone);
		DateTime transition = new DateTime(2009, 4, 28, 9, 00, 0, 0, dateTimeZone);
		DateTime end = new DateTime(2009, 4, 30, 11, 30, 0, 0, dateTimeZone);		
		
		assertThat(infection.getSusceptibility(), equalTo(new Interval(start,transition)));
		assertThat(infection.getInfectiousInterval(), equalTo(new Interval(transition,end)));
	}
}
