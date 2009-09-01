package com.hospitalbugs.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Test;


public class WardInfectionHistoryTest {

	@Test
	public void shouldNotReturnAnyValueForInfectionIfItDidNotOccurDuringrequestedInterval() {
		WardInfectionHistory history = new WardInfectionHistory();
		Infection infection = new InfectionBuilder().infectious(new Interval(100,109)).toInfection();
		history.add(infection,infection.getInfectiousInterval());
		
		assertThat(history.infectionSourcesFor(new Interval(109,200)), equalTo(Collections.<Infection,Duration>emptyMap()));
	}
	
}
