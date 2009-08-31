package com.madgag.intervals;

import static com.madgag.intervals.SimpleInterval.interval;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Instant;
import org.joda.time.Interval;

import com.hospitalbugs.model.EventMap;

public class JodaEventMap<V> {

	private EventMap eventMap = new EventMap();
	
	public void put(Interval interval, V value) {
		eventMap.put(simpleInterval(interval), value);
	}

	@SuppressWarnings("unchecked")
	public NavigableMap<Interval, V> subMapForEventsDuring(Interval interval) {
		NavigableMap<SimpleInterval, V> subMap = eventMap.subMapForEventsDuring(simpleInterval(interval));
		NavigableMap<Interval, V> jodaMap = new TreeMap<Interval, V>();
		for (Map.Entry<SimpleInterval, V> entry : subMap.entrySet()) {
			SimpleInterval simpleInterval = entry.getKey();
			jodaMap.put(new Interval((Instant) simpleInterval.getStart(),(Instant) simpleInterval.getEnd()), entry.getValue());
		}
		return jodaMap;
	}
	
	@SuppressWarnings("unchecked")
	private SimpleInterval simpleInterval(Interval interval) {
		return interval(interval.getStart(), interval.getEnd());
	}

}
