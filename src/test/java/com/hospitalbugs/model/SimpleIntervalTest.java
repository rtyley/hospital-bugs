package com.hospitalbugs.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class SimpleIntervalTest {

	@Test
	public void shouldReturnTrueIfPointIsContainedInInterval() {
		SimpleInterval<Integer> simpleInterval = new SimpleInterval<Integer>(10,20);
		assertThat(simpleInterval.contains(10), equalTo(true));
		assertThat(simpleInterval.contains(20), equalTo(false));
		assertThat(simpleInterval.contains(15), equalTo(true));
	}
	
	@Test
	public void shouldReturnTrueIfIntervalsOverlap() {
		assertThat(new SimpleInterval<Integer>(10,20).overlaps(new SimpleInterval<Integer>(15,16)), equalTo(true));
		assertThat(new SimpleInterval<Integer>(15,16).overlaps(new SimpleInterval<Integer>(10,20)), equalTo(true));

		assertThat(new SimpleInterval<Integer>(5,15).overlaps(new SimpleInterval<Integer>(10,20)), equalTo(true));
		assertThat(new SimpleInterval<Integer>(10,20).overlaps(new SimpleInterval<Integer>(5,15)), equalTo(true));

		assertThat(new SimpleInterval<Integer>(0,5).overlaps(new SimpleInterval<Integer>(10,20)), equalTo(false));
		assertThat(new SimpleInterval<Integer>(10,20).overlaps(new SimpleInterval<Integer>(0,5)), equalTo(false));
	}
	
	@Test
	public void shouldReturnFalseForOverlapIfIntervalsAbut() {
		assertThat(new SimpleInterval<Integer>(10,20).overlaps(new SimpleInterval<Integer>(20,30)), equalTo(false));
	}
	
}
