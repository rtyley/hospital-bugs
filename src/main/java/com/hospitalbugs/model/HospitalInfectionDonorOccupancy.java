package com.hospitalbugs.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Interval;

public class HospitalInfectionDonorOccupancy {

	private final Interval totalInterval;
	
	private final Map<Ward,WardInfectionHistory> wardInfectionHistories = new HashMap<Ward, WardInfectionHistory>();
	
	public HospitalInfectionDonorOccupancy(Collection<Infection> infections) {
		Interval totalInterval = null;
		for (Infection infection : infections) {
			Patient patient = infection.getPatient();
			Interval infectiousInterval = infection.getInfectiousInterval();
			totalInterval = union(totalInterval, infectiousInterval);
			Map<Interval, Ward> wardsOccupied = patient.getWardsOccupiedDuring(infectiousInterval);
			for (Map.Entry<Interval, Ward> wardStay : wardsOccupied.entrySet()) {
				Ward ward = wardStay.getValue();Interval interval = wardStay.getKey().overlap(infectiousInterval);
				infectionHistoryFor(ward).add(infection, interval);
			}
		}
		this.totalInterval=totalInterval;
	}
	
	public WardInfectionHistory infectionHistoryFor(Ward ward) {
		WardInfectionHistory wardInfectionHistory = wardInfectionHistories.get(ward);
		if (wardInfectionHistory==null) {
			wardInfectionHistories.put(ward, wardInfectionHistory=new WardInfectionHistory());
		}
		return wardInfectionHistory;
	}

	public Interval getTotalInterval() {
		return totalInterval;
	}

	public Collection<Ward> getAllWards() {
		return wardInfectionHistories.keySet();
	}
	
	private static Interval union(Interval a, Interval b) {
		if (a==null) {
			return b;
		}
		if (b==null) {
			return a;
		}
		if (a.contains(b)) {
			return a;
		}
		if (b.contains(a)) {
			return b;
		}
		long unionStart=(a.getStartMillis()<b.getStartMillis())?a.getStartMillis():b.getStartMillis();
		long unionEnd=(a.getEndMillis()>b.getEndMillis())?a.getEndMillis():b.getEndMillis();
			
		return new Interval(unionStart,unionEnd);
	}
}
