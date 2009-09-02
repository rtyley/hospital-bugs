package com.hospitalbugs.analysis;

import com.hospitalbugs.model.Ward;

public class LookupTableTransportFunction implements TransportFunction {

	@Override
	public float factorFor(Ward occupiedWard, Ward contributingWard) {
		return 0;
	}

}
