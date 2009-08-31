package com.hospitalbugs.model;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Interval;

public class Patient {

	private NavigableMap<Interval, Ward> wardStays = new TreeMap<Interval, Ward>();

	public Map<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return null;
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward);
	}

	
	private NavigableMap<LogInstant, SignificantInterval> subMapFor(LogInterval interval, boolean fromInclusive,
            boolean toInclusive) {
LogInstant start = significantInstants.floorKey(interval.getStart());
LogInstant end = significantInstants.ceilingKey(interval.getEnd());

return significantInstants.subMap(start==null?interval.getStart():start, fromInclusive, end==null?interval.getEnd():end, toInclusive);
}
}
