package com.hospitalbugs.model;

import static com.hospitalbugs.model.BoundClosure.CLOSED;
import static com.hospitalbugs.model.BoundClosure.OPEN;
import static com.hospitalbugs.model.SimpleInterval.instantInterval;

import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventMap<InstantType extends Comparable<InstantType>, EventType> {

	private NavigableMap<SimpleInterval<InstantType>, EventType> events = new TreeMap<SimpleInterval<InstantType>, EventType>(SimpleInterval.OverlapIsEqualityComparator.<InstantType>instance());

	public EventType getEventAt(InstantType instant) {
		Map.Entry<SimpleInterval<InstantType>, EventType> floorEntry = entryForEventStartingAtOrBefore(instant);
		if (floorEntry==null) {
			return null;
		}
		return floorEntry.getKey().contains(instant) ? floorEntry.getValue() : null;
	}
	
	public EventType getLatestSignificantIntervalStartingAtOrBefore(InstantType instant) {
		Map.Entry<SimpleInterval<InstantType>, EventType> entry = entryForEventStartingAtOrBefore(instant);
		return entry==null?null:entry.getValue();
	}

	private Map.Entry<SimpleInterval<InstantType>, EventType> entryForEventStartingAtOrBefore(InstantType instant) {
		return events.floorEntry(instantInterval(instant, CLOSED));
	}
	
	public Collection<EventType> getEventsDuring(InstantType start, InstantType end) {
		return getEventsDuring(new SimpleInterval<InstantType>(start, end));
	}
	
	public Collection<EventType> getEventsDuring(SimpleInterval<InstantType> interval) {
		return subMapFor(interval).values();
	}
	
	public NavigableMap<SimpleInterval<InstantType>, EventType> subMap(InstantType fromKey, InstantType toKey) {
		TreeMap<SimpleInterval<InstantType>, EventType> copyMap = new TreeMap<SimpleInterval<InstantType>, EventType>(SimpleInterval.OverlapIsEqualityComparator.<InstantType>instance());
		copyMap.putAll(internal_subMap(fromKey, toKey));
		return copyMap;
	}
	
	private NavigableMap<SimpleInterval<InstantType>, EventType> internal_subMap(InstantType fromKey, InstantType toKey) {
		return events.subMap(instantInterval(fromKey, CLOSED), true, instantInterval(toKey, OPEN), true);
	}
	
	private NavigableMap<SimpleInterval<InstantType>, EventType> subMapFor(SimpleInterval<InstantType> interval) {
		return internal_subMap(interval.getStart(), interval.getEnd());
	}

	public void put(SimpleInterval<InstantType> interval, EventType event) {
		checkCanAddEventTo(interval);
		addWithoutChecking(interval, event);
	}
	
	public void overrideWith(SimpleInterval<InstantType> interval, EventType event) {
		subMapFor(interval).clear();
		addWithoutChecking(interval, event);
	}

	private void checkCanAddEventTo(SimpleInterval<InstantType> interval) {
		if (events.containsKey(interval)) {
			throw new IllegalArgumentException();
		}
	}

	private void addWithoutChecking(SimpleInterval<InstantType> interval, EventType event) {
		events.put(interval, event);
	}
	
	public SimpleInterval<InstantType> getInterval() {
		return events.isEmpty()?null:new SimpleInterval<InstantType>(events.firstKey().getStart(),events.lastKey().getEnd());
	}

	public Collection<EventType> values() {
		return events.values();
	}
}
