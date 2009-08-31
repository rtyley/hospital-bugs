package com.hospitalbugs.model;

public interface IntervalTypeAdaptor<InstantType extends Comparable<InstantType>,EventType> {

	public SimpleInterval<InstantType> getIntervalFor(EventType event);
}
