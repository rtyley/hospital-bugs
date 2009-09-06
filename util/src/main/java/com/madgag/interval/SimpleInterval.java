package com.madgag.interval;

import static com.madgag.interval.BoundType.MAX;
import static com.madgag.interval.BoundType.MIN;
import static com.madgag.interval.Closure.CLOSED;
import static com.madgag.interval.Closure.OPEN;

import java.util.Comparator;


public class SimpleInterval<T extends Comparable<T>> {

	private final Bound<T> startBound, endBound;

	public SimpleInterval(T start, T end) {
		this(start,CLOSED, end, OPEN);
	}
	
	public SimpleInterval(T start, Closure startClosure, T end, Closure endClosure) {
		if (start.compareTo(end) > 0) {
			throw new IllegalArgumentException();
		}
		this.startBound = new Bound<T>(BoundTypeWithClosure.get(MIN, startClosure), start);
		this.endBound = new Bound<T>(BoundTypeWithClosure.get(MAX, endClosure), end);
	}
	
	public static <T extends Comparable<T>> SimpleInterval<T> interval(T start, T end) {
		return new SimpleInterval<T>(start,end);
	}
	
	public static <T extends Comparable<T>> SimpleInterval<T> instantInterval(T instant, Closure closure) {
		return new SimpleInterval<T>(instant, closure, instant, closure);
	}
	
	public T getStart() {
		return startBound.getValue();
	}
	
	public T getEnd() {
		return endBound.getValue();
	}
	
	public boolean contains(T point) {
		return startBound.encloses(point) && endBound.encloses(point);
	}

	public boolean isAfter(T point) {
		return !startBound.encloses(point);
	}
	
	public boolean isBefore(T point) {
		return !endBound.encloses(point);
	}
	
	public boolean isAfter(SimpleInterval<T> other) {
		return isAfter(other.getEnd());
	}
	
	public boolean isBefore(SimpleInterval<T> other) {
		return isBefore(other.getStart());
	}
	
	public boolean overlaps(SimpleInterval<T> other) {
		return !isBefore(other) && !other.isBefore(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endBound == null) ? 0 : endBound.hashCode());
		result = prime * result
				+ ((startBound == null) ? 0 : startBound.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleInterval other = (SimpleInterval) obj;
		if (endBound == null) {
			if (other.endBound != null)
				return false;
		} else if (!endBound.equals(other.endBound))
			return false;
		if (startBound == null) {
			if (other.startBound != null)
				return false;
		} else if (!startBound.equals(other.startBound))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return startBound+" - "+ endBound;
	}

	
	public static class OverlapIsEqualityComparator<T extends Comparable<T>> implements Comparator<SimpleInterval<T>> {
		
		@SuppressWarnings("unchecked")
		private static OverlapIsEqualityComparator INSTANCE = new OverlapIsEqualityComparator();
		
	    @SuppressWarnings("unchecked")
	    public static final <T extends Comparable<T>> Comparator<SimpleInterval<T>> instance() {
	        return (Comparator<SimpleInterval<T>>) INSTANCE;
	    }

		@Override
		public int compare(SimpleInterval<T> o1, SimpleInterval<T> o2) {
			if (o1.overlaps(o2)) {
				return 0;
			}
			return o1.isBefore(o2)?-1:1;
		}
	}
}
