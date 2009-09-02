package com.hospitalbugs.analysis;

import com.hospitalbugs.model.Ward;

public interface TransportFunction {

	public float factorFor(Ward occupiedWard, Ward contributingWard);

}
