package com.hospitalbugs.analysis;

import static com.google.common.collect.Maps.transformValues;
import static com.hospitalbugs.model.StandardisedMicrobialLoad.ZERO;
import static org.joda.time.Duration.standardDays;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;
import com.hospitalbugs.model.WardInfectionHistory;
import com.madgag.intervals.JodaEventMap;

public class HospitalMicrobialLoadUsingLambdaModel implements HospitalMicrobialLoad {

	private Map<Ward,WardData> map;
	private TransportFunction transportFunction;
	private Duration stepDuration = standardDays(1);

	public HospitalMicrobialLoadUsingLambdaModel(HospitalInfectionDonorOccupancy donorOccupancy, float lambda, TransportFunction transportFunction) {
		this.transportFunction = transportFunction;
		
		Interval totalInterval = donorOccupancy.getTotalInterval();
		map = new HashMap<Ward,WardData>();
		Interval priorInterval = null;
		for (Interval interval = totalInterval.withDurationAfterStart(stepDuration);
			!interval.isAfter(totalInterval.getEnd().plus(standardDays(5)));
			interval=stepDuration.toIntervalFrom(interval.getEnd())) {
			for (Ward ward : donorOccupancy.getAllWards()) {
				WardData wardData = getOrInitialiseWardDataFor(ward);
				StandardisedMicrobialLoad load = ZERO;
				if (priorInterval!=null) {
					load = load.addWithScalar(wardData.microbialLoadFor(priorInterval), lambda);
				}
				WardInfectionHistory wardHistory = donorOccupancy.infectionHistoryFor(ward);
				load=load.add(StandardisedMicrobialLoad.of(unitMicrobialLoadFor(wardHistory.infectionSourcesFor(interval))));
				wardData.set(load,interval);
				priorInterval = interval;
			}
		}
	}

	private Map<Infection, Float> unitMicrobialLoadFor(
			Map<Infection, Duration> infectionSources) {
		Map<Infection, Float> loadMap = transformValues(infectionSources, new Function<Duration, Float>() {
			public Float apply(Duration duration) {
				return 1f; // TODO do anything about partial duration or interval?
			}
		} );
		return loadMap;
	}

	private WardData getOrInitialiseWardDataFor(Ward ward) {
		WardData wardData = map.get(ward);
		if (wardData==null) {
			map.put(ward, wardData=new WardData());
		}
		return wardData;
	}

	@Override
	public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval) {
		StandardisedMicrobialLoad totalLoad = ZERO;
		for (Map.Entry<Ward, WardData> contributingEntry : map.entrySet()) {
			Ward contributingWard = contributingEntry.getKey();WardData wardData = contributingEntry.getValue();
			totalLoad = totalLoad.addWithScalar(wardData.microbialLoadFor(wardOccupationInterval), transportFunction.factorFor(ward, contributingWard));
		}
		return totalLoad;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+map.size()+" wards]";
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
		
		@Override
		public String toString() {
			return getClass().getSimpleName()+"[for " +data.getInterval()+" : "+ data.toString() +"]";
		}
	}

}
