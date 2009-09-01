package com.hospitalbugs.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.madgag.intervals.JodaEventSet;

public class WardInfectionHistory {

	private final Map<Infection,JodaEventSet> infections = new HashMap<Infection, JodaEventSet>(); 
	
	public Map<Infection, Duration> infectionSourcesFor(Interval interval) {
		Map<Infection, Duration> infectionDurations = new HashMap<Infection, Duration>();
		for (Map.Entry<Infection,JodaEventSet> entry : infections.entrySet()) {
			Duration infectionDurationDuringInterval = entry.getValue().totalSubSetDurationFor(interval);
			if (!infectionDurationDuringInterval.equals(Duration.ZERO)) {
				infectionDurations.put(entry.getKey(), infectionDurationDuringInterval);
			}
		}
		return infectionDurations;
	}

	public void add(Infection infection, Interval interval) {
		JodaEventSet infectionEventSet = infections.get(infection);
		if (infectionEventSet==null) {
			infections.put(infection, infectionEventSet = new JodaEventSet());
		}
		infectionEventSet.add(interval);
	}

}
