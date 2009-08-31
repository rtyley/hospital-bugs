package com.hospitalbugs.model;

import java.util.Comparator;


public class SimpleInterval<T extends Comparable<T>> {

	private final T start;
	private final T end;

	public SimpleInterval(T start, T end) {
		if (start.compareTo(end) > 0) {
			throw new IllegalArgumentException();
		}
		this.start = start;
		this.end = end;
	}
	
	public T getStart() {
		return start;
	}
	
	public T getEnd() {
		return end;
	}
	
	public boolean contains(T point) {
		return start.compareTo(point) <= 0 && point.compareTo(end)<0;
	}

	public boolean isAfter(T point) {
		return start.compareTo(point) > 0;
	}
	
	public boolean isBefore(T point) {
		return end.compareTo(point) <= 0;
	}
	
	public boolean isAfter(SimpleInterval<T> other) {
		return isAfter(other.getEnd());
	}
	
	public boolean isBefore(SimpleInterval<T> other) {
		return isBefore(other.getStart());
	}
	
	public boolean overlaps(SimpleInterval<T> other) {
		return !isBefore(other) && !isAfter(other);
	}

	
	@Override
	public String toString() {
		return "["+ start+" - "+ end +")";
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
