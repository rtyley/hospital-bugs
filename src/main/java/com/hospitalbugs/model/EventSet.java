package com.hospitalbugs.model;

import static com.hospitalbugs.model.SimpleInterval.instantInterval;

import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class EventSet<InstantType extends Comparable<InstantType>, EventType> {

	private final IntervalTypeAdaptor<InstantType, EventType> adaptor;
	
	public EventSet(IntervalTypeAdaptor<InstantType, EventType> adaptor) {
		this.adaptor = adaptor;
	}

	private NavigableMap<SimpleInterval<InstantType>, EventType> events = new TreeMap<SimpleInterval<InstantType>, EventType>(SimpleInterval.OverlapIsEqualityComparator.<InstantType>instance());

	public EventType getSignificantIntervalAt(InstantType instant) {
		Map.Entry<SimpleInterval<InstantType>, EventType> floorEntry = events.floorEntry(instantInterval(instant));
		if (floorEntry==null) {
			return null;
		}
		EventType event = floorEntry.getValue();
		return adaptor.getIntervalFor(event).contains(instant) ? event : null;
	}
	
	public EventType getLatestSignificantIntervalStartingAtOrBefore(InstantType instant) {
		Map.Entry<SimpleInterval<InstantType>, EventType> entry = events.floorEntry(instantInterval(instant));
		return entry==null?null:entry.getValue();
	}
	
	public Collection<EventType> getSignificantIntervalsDuring(InstantType start, InstantType end) {
		return getSignificantIntervalsDuring(new SimpleInterval<InstantType>(start, end));
	}
	
	public Collection<EventType> getSignificantIntervalsDuring(SimpleInterval<InstantType> interval) {
		return events.subMap(instantInterval(interval.getStart()), true, instantInterval(interval.getEnd()), false).values();
	}

	public void add(EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		checkCanAddEventTo(interval);
		addWithoutChecking(interval, event);
	}
	
	public void overrideWith(EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		for (EventType otherSignificantInterval : getSignificantIntervalsDuring(interval)) {
			remove(otherSignificantInterval);
		}
		addWithoutChecking(interval, event);
	}

	private void checkCanAddEventTo(SimpleInterval<InstantType> interval) {
		if (events.containsKey(interval)) {
			throw new IllegalArgumentException();
		}
//		ConcurrentNavigableMap<SimpleInterval<InstantType>, EventType> eventsDuringInterval = eventsOccurringDuring(interval);
//		if (!eventsDuringInterval.isEmpty()) {
//			TreeSet<EventType> currentTenants = new TreeSet<EventType>(eventsDuringInterval.values());
//			throw new IllegalArgumentException("Can't add event with interval " + interval + " - interval already occupied by " + currentTenants);
//		}
	}

	private void addWithoutChecking(SimpleInterval<InstantType> interval2, EventType event) {
		SimpleInterval<InstantType> interval = adaptor.getIntervalFor(event);
		events.put(interval, event);
	}

//	private ConcurrentNavigableMap<SimpleInterval<InstantType>, EventType> eventsOccurringDuring(SimpleInterval<InstantType> interval) {
//		// return eventsPreciselyWithin(intervalEnclosingEventsOccuringDuring(interval));
//	}
	
	/* This will return every event that has either it's start or end within the interval
	 * Note this DOES NOT include an event spanning a period that completely encloses the interval
	 */
//	private ConcurrentNavigableMap<SimpleInterval<InstantType>, EventType> eventsPreciselyWithin(SimpleInterval<InstantType> interval) {
//		return events.subMap(interval.getStart(), interval.getEnd());
//	}

//	private SimpleInterval<InstantType> intervalEnclosingEventsOccuringDuring(SimpleInterval<InstantType> interval) {
//		InstantType start = interval.getStart(), end = interval.getEnd();
//		return new SimpleInterval<InstantType>(
//				valueOrDefault(events.floorKey(start), start),
//				valueOrDefault(events.ceilingKey(end), end));
//	}

	private InstantType valueOrDefault(InstantType val, InstantType def) {
		return val == null ? def : val;
	}

	private void remove(EventType event) {
		events.remove(adaptor.getIntervalFor(event));
	}
	
	public SimpleInterval<InstantType> getInterval() {
		return events.isEmpty()?null:new SimpleInterval<InstantType>(events.firstKey().getStart(),events.lastKey().getEnd());
	}

//	public int countOccurencesDuring(SimpleInterval<InstantType> interval) {
//		NavigableMap<SimpleInterval<InstantType>, EventType> subMap = subMapFor(interval,true,true);
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
