package com.madgag.intervals;

import static com.madgag.intervals.SimpleInterval.interval;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.joda.time.Interval;
import org.joda.time.ReadableInstant;


@SuppressWarnings("unchecked")
public class JodaEventMap<V> {

	private EventMap eventMap = new EventMap();
	
	public void put(Interval interval, V value) {
		eventMap.put(simpleInterval(interval), value);
	}

	public Map<Interval, V> subMapForEventsDuring(Interval interval) {
		NavigableMap<SimpleInterval, V> subMap = eventMap.subMapForEventsDuring(simpleInterval(interval));
		Map<Interval, V> jodaMap = new HashMap<Interval, V>();
		for (Map.Entry<SimpleInterval, V> entry : subMap.entrySet()) {
			SimpleInterval simpleInterval = entry.getKey();
			jodaMap.put(jodaInterval(simpleInterval), entry.getValue());
		}
		return jodaMap;
	}

	private Interval jodaInterval(SimpleInterval simpleInterval) {
		return new Interval((ReadableInstant) simpleInterval.getStart(),(ReadableInstant) simpleInterval.getEnd());
	}
	
	private SimpleInterval simpleInterval(Interval interval) {
		return interval(interval.getStart(), interval.getEnd());
	}

}
