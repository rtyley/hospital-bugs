package com.madgag.intervals;

import static java.lang.Boolean.TRUE;

import java.util.Set;

public class EventSet<InstantType extends Comparable<InstantType>> {

	private final EventMap<InstantType, Boolean> map = new EventMap<InstantType, Boolean>();
	
	public void add(SimpleInterval<InstantType> interval) {
		map.put(interval, TRUE);
	}

	public Set<SimpleInterval<InstantType>> subSet(SimpleInterval<InstantType> simpleInterval) {
		return map.subMapForEventsDuring(simpleInterval).keySet();
	}

}
