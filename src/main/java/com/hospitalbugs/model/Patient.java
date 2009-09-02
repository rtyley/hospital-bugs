package com.hospitalbugs.model;

import java.util.Map;

import org.joda.time.Interval;

import com.madgag.intervals.JodaEventMap;

public class Patient {

	private JodaEventMap<Ward> wardStays = new JodaEventMap<Ward>();
	private final String id;

	public Patient(String id) {
		this.id = id;
	}

	public Map<Interval, Ward> getWardsOccupiedDuring(Interval interval) {
		return wardStays.subMapForEventsDuring(interval);
	}

	public void addWardStay(Ward ward, Interval interval) {
		wardStays.put(interval, ward );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+id+"]";
	}
	
}
