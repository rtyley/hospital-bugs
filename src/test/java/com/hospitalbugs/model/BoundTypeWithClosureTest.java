package com.hospitalbugs.model;

import static com.hospitalbugs.model.BoundTypeWithClosure.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;


public class BoundTypeWithClosureTest {

	@Test
	public void shouldBeGoodDammit() {
		assertThat(MIN_OPEN.isOnside(0), equalTo(false));
		assertThat(MAX_OPEN.isOnside(0), equalTo(false));
		assertThat(MIN_CLOSED.isOnside(0), equalTo(true));
		assertThat(MAX_CLOSED.isOnside(0), equalTo(true));

		assertThat(MIN_OPEN.isOnside(-1), equalTo(false));
		assertThat(MAX_OPEN.isOnside(-1), equalTo(true));
		assertThat(MIN_OPEN.isOnside(1), equalTo(true));
		assertThat(MAX_OPEN.isOnside(1), equalTo(false));

		assertThat(MIN_CLOSED.isOnside(-1), equalTo(false));
		assertThat(MAX_CLOSED.isOnside(-1), equalTo(true));
		assertThat(MIN_CLOSED.isOnside(1), equalTo(true));
		assertThat(MAX_CLOSED.isOnside(1), equalTo(false));
		
		
	}
}
