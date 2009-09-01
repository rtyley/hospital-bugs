package com.hospitalbugs.analysis;

import static com.google.common.collect.Maps.transformValues;
import static com.hospitalbugs.model.StandardisedMicrobialLoad.ZERO;
import static org.joda.time.Duration.standardDays;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;
import com.madgag.intervals.JodaEventMap;

public class HospitalMicrobialLoadUsingLambdaModel implements HospitalMicrobialLoad {

	private Map<Ward,WardData> map;
	private float transportFactor;
	private Duration stepDuration = standardDays(1);

	public HospitalMicrobialLoadUsingLambdaModel(HospitalInfectionDonorOccupancy donorOccupancy, float lambda, float transportFactor) {
		this.transportFactor = transportFactor;
		
		Interval totalInterval = donorOccupancy.getTotalInterval();
		map = new HashMap<Ward,WardData>();
		Interval priorInterval = null;
		for (Interval interval = totalInterval.withDurationAfterStart(stepDuration);
			!interval.isAfter(totalInterval.getEnd().plus(Duration.standardDays(5)));
			interval=stepDuration.toIntervalFrom(interval.getEnd())) {
			for (Ward ward : donorOccupancy.getAllWards()) {
				WardData wardData = map.get(ward);
				if (wardData==null) {
					map.put(ward, wardData=new WardData());
				}
				StandardisedMicrobialLoad load = ZERO;
				if (priorInterval!=null) {
					load = load.addWithScalar(wardData.microbialLoadFor(priorInterval), lambda);
				}
				Map<Infection, Duration> infectionSources = donorOccupancy.infectionHistoryFor(ward).infectionSourcesFor(interval);
				Map<Infection, Float> loadMap = transformValues(infectionSources, new Function<Duration, Float>() {
					public Float apply(Duration duration) {
						return 1f; // TODO do anything about partial duration or interval?
					}
				} );
				load=load.add(StandardisedMicrobialLoad.of(loadMap));
				wardData.set(load,interval);
				priorInterval = interval;
			}
		}
	}

	@Override
	public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval) {
		StandardisedMicrobialLoad totalLoad = ZERO;
		for (Map.Entry<Ward, WardData> entry : map.entrySet()) {
			Ward contributingWard = entry.getKey();WardData wardData = entry.getValue();
			if (contributingWard.equals(ward)) {
				totalLoad = totalLoad.add(wardData.microbialLoadFor(wardOccupationInterval));
			} else {
				totalLoad = totalLoad.addWithScalar(wardData.microbialLoadFor(wardOccupationInterval), transportFactor);
			}
		}
		return totalLoad;
	}
	
	static class WardData {
		JodaEventMap<StandardisedMicrobialLoad> data = new JodaEventMap<StandardisedMicrobialLoad>();
		
		StandardisedMicrobialLoad microbialLoadFor(Interval wardOccupationInterval) {
			StandardisedMicrobialLoad totalLoad = ZERO;
			Collection<StandardisedMicrobialLoad> loads = data.subMapForEventsDuring(wardOccupationInterval).values();
			for (StandardisedMicrobialLoad load : loads) {
				totalLoad = totalLoad.add(load);
			}
			return totalLoad;
		}

		public void set(StandardisedMicrobialLoad load, Interval interval) {
			data.put(interval, load);
		}
	}

}
