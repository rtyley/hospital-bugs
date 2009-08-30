package com.hospitalbugs.model;

import org.joda.time.Interval;

public interface HospitalMicrobialLoad {

	public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval);

}
