package com.hospitalbugs.model;

import java.util.Collection;
import java.util.Map;

import org.joda.time.Interval;

public class HospitalInfectionDonorOccupancy {

	Map<Ward,WardInfectionHistory> warda;
	
	public HospitalInfectionDonorOccupancy(Collection<Infection> infections) {
		for (Infection infection : infections) {
			Patient patient = infection.getPatient();
			Interval infectiousInterval = infection.getInfectiousInterval();
			Map<Interval, Ward> wardOccupancies = patient.getWardOccupanciesDuring(infectiousInterval);
			for (Map.Entry<Interval, Ward> waa : wardOccupancies.entrySet()) {
				Ward ward = waa.getValue();
				warda.get(ward).add(infection, waa.getKey());
			}
		}
	}
	
	public WardInfectionHistory infectionHistoryFor(Ward ward) {
		return warda.get(ward);
	}

	public Interval getTotalInterval() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Ward> getAllWards() {
		// TODO Auto-generated method stub
		return null;
	}
}
