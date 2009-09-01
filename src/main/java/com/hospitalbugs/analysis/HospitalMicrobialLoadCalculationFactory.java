package com.hospitalbugs.analysis;

import static com.google.common.collect.Maps.transformValues;
import static org.joda.time.Duration.standardDays;

import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.common.base.Function;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;

public class HospitalMicrobialLoadCalculationFactory {

	public HospitalMicrobialLoad simpleModelWithImmediateFallOffOfMicrobialLoad(final HospitalInfectionDonorOccupancy donorOccupancy) {
		return new HospitalMicrobialLoad() {
			@Override
			public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval) {
				Map<Infection, Duration> infectionSources = donorOccupancy.infectionHistoryFor(ward).infectionSourcesFor(wardOccupationInterval);
				Map<Infection, Float> loadMap = transformValues(infectionSources, new Function<Duration, Float>() {
					public Float apply(Duration duration) {
						return ((float) duration.getMillis()) / standardDays(1).getMillis();
					}
				} );
				return StandardisedMicrobialLoad.of(loadMap);
			}
		};
	}
	
	public HospitalMicrobialLoad lambdaModel(final HospitalInfectionDonorOccupancy donorOccupancy, float lambda, float transport) {
		return new HospitalMicrobialLoadUsingLambdaModel(donorOccupancy, lambda, transport);
	}

}
