package com.hospitalbugs.model;

import java.util.Map;

import org.joda.time.Interval;

import com.madgag.intervals.JodaEventMap;

public class Patient {

	private JodaEventMap<Ward> wardStays = new JodaEventMap<Ward>();

	public Map<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return wardStays.subMapForEventsDuring(interval);
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward );
	}

	
}
