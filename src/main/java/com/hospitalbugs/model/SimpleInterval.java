package com.hospitalbugs.model;


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
	
	@Override
	public String toString() {
		return "["+ start+" - "+ end +")";
	}
}
