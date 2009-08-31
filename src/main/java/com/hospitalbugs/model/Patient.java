package com.hospitalbugs.model;

import java.util.NavigableMap;

import org.joda.time.Interval;

import com.madgag.intervals.JodaEventMap;

public class Patient {

	private JodaEventMap<Ward> wardStays = new JodaEventMap<Ward>();

	public NavigableMap<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return wardStays.subMapForEventsDuring(interval);
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward );
	}

	
}
