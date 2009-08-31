package com.hospitalbugs.analysis;

import org.joda.time.Interval;

import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.model.Ward;

public interface HospitalMicrobialLoad {

	public StandardisedMicrobialLoad microbialLoadFor(Ward ward, Interval wardOccupationInterval);

}
