package com.hospitalbugs.model;

import static com.hospitalbugs.model.SimpleInterval.interval;

import java.util.Map;

import org.joda.time.Interval;

public class Patient {

	@SuppressWarnings("unchecked")
	private EventMap wardStays = new EventMap();

	public Map<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return wardStays.;
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval(interval.getStart(), interval.getEnd()), ward );
	}

	
}
