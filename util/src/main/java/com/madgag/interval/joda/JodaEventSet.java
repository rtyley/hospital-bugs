package com.madgag.interval.joda;

import static com.madgag.interval.SimpleInterval.interval;

import java.util.Set;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

import com.madgag.interval.EventSet;
import com.madgag.interval.SimpleInterval;


@SuppressWarnings("unchecked")
public class JodaEventSet {

	private EventSet eventSet = new EventSet();
	
	public boolean add(Interval interval) {
		eventSet.add(simpleInterval(interval));
		return false;
	}
	
	private Interval jodaInterval(SimpleInterval simpleInterval) {
		return new Interval((ReadableInstant) simpleInterval.getStart(),(ReadableInstant) simpleInterval.getEnd());
	}
	
	private SimpleInterval simpleInterval(Interval interval) {
		return interval(interval.getStart(), interval.getEnd());
	}


	public Duration totalSubSetDurationFor(Interval interval) {
		Set<SimpleInterval> simpleIntervals = eventSet.subSet(simpleInterval(interval));
		long total = 0;
		for (SimpleInterval simpleInterval : simpleIntervals) {
			total += jodaInterval(simpleInterval).overlap(interval).toDurationMillis();
		}
		return new Duration(total);
	}

}
