package com.hospitalbugs.model;

import static com.google.common.collect.Maps.transformValues;
import static org.joda.time.Duration.standardDays;

import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.common.base.Function;

public class HospitalMicrobialLoadCalculationFactory {

	public HospitalMicrobialLoad simpleModelWithImmediateFallOffOfMicrobialLoad(final HospitalInfectionDonorOccupancy donerOccupancy) {
		return new HospitalMicrobialLoad() {
			@Override
			public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval) {
				Map<Infection, Duration> infectionSources = donerOccupancy.infectionHistoryFor(ward).infectionSourcesFor(wardOccupationInterval);
				Map<Infection, Float> loadMap = transformValues(infectionSources, new Function<Duration, Float>() {
					public Float apply(Duration duration) {
						return ((float) duration.getMillis()) / standardDays(1).getMillis();
					}
				} );
				return StandardisedMicrobialLoad.of(loadMap);
			}
		};
	}
	
	public HospitalMicrobialLoad lambdaModelWithNoTransport(final HospitalInfectionDonorOccupancy donerOccupancy, float lambda) {
		return new HospitalMicrobialLoadUsingLambdaModel(donerOccupancy, lambda);
	}

}
