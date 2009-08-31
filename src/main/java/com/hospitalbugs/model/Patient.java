package com.hospitalbugs.model;

import java.util.Map;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class Patient {

	private EventSet wardStays = new EventSet(null);

	public Map<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return null;
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward);
	}

	
}
