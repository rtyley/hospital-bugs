package com.hospitalbugs.model;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class EventSet<InstantType extends Comparable<InstantType>, EventType> {

	private final IntervalTypeAdaptor<InstantType, EventType> adaptor;
	
	public EventSet(IntervalTypeAdaptor<InstantType, EventType> adaptor) {
		this.adaptor = adaptor;
	}

	private ConcurrentNavigableMap<InstantType, EventType> events = new ConcurrentSkipListMap<InstantType, EventType>();

	public EventType getSignificantIntervalAt(InstantType instant) {
		Map.Entry<InstantType, EventType> floorEntry = events.floorEntry(instant);
		if (floorEntry==null) {
			return null;
		}
		EventType event = floorEntry.getValue();
		return adaptor.getIntervalFor(event).contains(instant) ? event : null;
	}
	
	public EventType getLatestSignificantIntervalStartingAtOrBefore(InstantType instant) {
		Map.Entry<InstantType, EventType> entry = events.floorEntry(instant);
		return entry==null?null:entry.getValue();
	}
	
	public Collection<EventType> getSignificantIntervalsDuring(InstantType start, InstantType end) {
		return getSignificantIntervalsDuring(new SimpleInterval<InstantType>(start, end));
	}
	
	public Collection<EventType> getSignificantIntervalsDuring(SimpleInterval<InstantType> interval) {
		return deDup(eventsOccurringDuring(interval).values());
	}

	private Collection<EventType> deDup(Collection<EventType> values) {
		Deque<EventType> events = new ArrayDeque<EventType>(values.size()/2);
		for (EventType e : values) {
			if (e!=events.peekLast()) {
				events.add(e);
			}
		}
		return events;
	}

	public void add(EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		checkCanAddEventTo(interval);
		addWithoutChecking(interval, event);
	}
	
	public void overrideWith(EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		for (EventType otherSignificantInterval : eventsOccurringDuring(interval).values()) {
			remove(otherSignificantInterval);
		}
		addWithoutChecking(interval, event);
	}

	private void checkCanAddEventTo(SimpleInterval<InstantType> interval) {
		if (events.containsKey(interval.getStart()) || events.containsKey(interval.getEnd())) {
			throw new IllegalArgumentException();
		}
		ConcurrentNavigableMap<InstantType, EventType> eventsDuringInterval = eventsOccurringDuring(interval);
		if (!eventsDuringInterval.isEmpty()) {
			TreeSet<EventType> currentTenants = new TreeSet<EventType>(eventsDuringInterval.values());
			throw new IllegalArgumentException("Can't add event with interval " + interval + " - interval already occupied by " + currentTenants);
		}
	}

	private void addWithoutChecking(SimpleInterval<InstantType> interval2, EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		events.put(interval.getStart(), event);
		events.put(interval.getEnd(), event);
	}

	private ConcurrentNavigableMap<InstantType, EventType> eventsOccurringDuring(SimpleInterval<InstantType> interval) {
		return eventsPreciselyWithin(intervalEnclosingEventsOccuringDuring(interval));
	}
	
	/* This will return every event that has either it's start or end within the interval
	 * Note this DOES NOT include an event spanning a period that completely encloses the interval
	 */
	private ConcurrentNavigableMap<InstantType, EventType> eventsPreciselyWithin(SimpleInterval<InstantType> interval) {
		return events.subMap(interval.getStart(), interval.getEnd());
	}

	private SimpleInterval<InstantType> intervalEnclosingEventsOccuringDuring(SimpleInterval<InstantType> interval) {
		InstantType start = interval.getStart(), end = interval.getEnd();
		return new SimpleInterval<InstantType>(
				valueOrDefault(events.floorKey(start), start),
				valueOrDefault(events.ceilingKey(end), end));
	}

	private InstantType valueOrDefault(InstantType val, InstantType def) {
		return val == null ? def : val;
	}

	private void remove(EventType event) {
		SimpleInterval<InstantType> otherLogInterval = adaptor.getIntervalFor(event);
		InstantType start = otherLogInterval.getStart(), end = otherLogInterval.getEnd();
		if (events.get(start)==event) { // why this check?
			events.remove(start);
		}
		if (events.get(end)==event) { // why this check?
			events.remove(end);
		}
	}
	
	public SimpleInterval<InstantType> getInterval() {
		return events.isEmpty()?null:new SimpleInterval<InstantType>(events.firstKey(),events.lastKey());
	}

//	public int countOccurencesDuring(SimpleInterval<InstantType> interval) {
//		NavigableMap<InstantType, EventType> subMap = subMapFor(interval,true,true);
//		int uniqueCount=0;
//		EventType prior=null;
//		for (EventType event : subMap.values()) {
//			SimpleInterval<InstantType> spanOfThing = adaptor.getIntervalFor(event);
//			if (!(spanOfThing.isBefore(interval) || spanOfThing.isAfter(interval))) {
//				if (prior!=event) {
//					++uniqueCount;
//					prior=event;
//				}
//			}
//		}
//		return uniqueCount;
//	}
}
