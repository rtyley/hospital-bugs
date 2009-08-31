package com.hospitalbugs.model;

public class Bound<T extends Comparable<T>> {

	private final T val;
	private final BoundTypeWithClosure boundTypeWithClosure;

	public Bound(BoundTypeWithClosure boundTypeWithClosure, T val) {
		this.boundTypeWithClosure = boundTypeWithClosure;
		this.val = val;
	}

	public T getValue() {
		return val;
	}
	
	public boolean encloses(T point) {
		int comparison = val.compareTo(point);
		return boundTypeWithClosure.isOnside(comparison);
	}

	public BoundTypeWithClosure getBound() {
		return boundTypeWithClosure;
	}
	
	@Override
	public String toString() {
		if (boundTypeWithClosure.getBoundType()==BoundType.MIN) {
			return boundTypeWithClosure.getTextRepresentation()+val;
		} else {
			return val + boundTypeWithClosure.getTextRepresentation();
		}
	}
}
