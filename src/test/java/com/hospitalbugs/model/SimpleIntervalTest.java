package com.hospitalbugs.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;


public class SimpleIntervalTest {

	@Test
	public void shouldreturnTrueIfPointIsContainedInInterval() {
		SimpleInterval<Integer> simpleInterval = new SimpleInterval<Integer>(10,20);
		assertThat(simpleInterval.contains(10), equalTo(true));
		assertThat(simpleInterval.contains(20), equalTo(false));
		assertThat(simpleInterval.contains(15), equalTo(true));
	}
}
