package com.hospitalbugs.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.madgag.interval.collections.joda.JodaIntervalSet;

public class WardInfectionHistory {

	private final Map<Infection, JodaIntervalSet> infections = new HashMap<Infection, JodaIntervalSet>();
	
	public Map<Infection, Duration> infectionSourcesFor(Interval interval) {
		Map<Infection, Duration> infectionDurations = new HashMap<Infection, Duration>();
		for (Map.Entry<Infection, JodaIntervalSet> entry : infections.entrySet()) {
			Duration infectionDurationDuringInterval = entry.getValue().totalSubSetDurationFor(interval);
			if (!infectionDurationDuringInterval.equals(Duration.ZERO)) {
				infectionDurations.put(entry.getKey(), infectionDurationDuringInterval);
			}
		}
		return infectionDurations;
	}

	public void add(Infection infection, Interval interval) {
		JodaIntervalSet infectionIntervalSet = infections.get(infection);
		if (infectionIntervalSet ==null) {
			infections.put(infection, infectionIntervalSet = new JodaIntervalSet());
		}
		infectionIntervalSet.add(interval);
	}

}
