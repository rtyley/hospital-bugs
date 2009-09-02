package com.hospitalbugs.analysis;

import com.hospitalbugs.model.Ward;

public class ConstantTransportFunction implements TransportFunction {

	private float transportFactor;
	
	public ConstantTransportFunction(float transportFactor) {
		this.transportFactor = transportFactor;
	}

	@Override
	public float factorFor(Ward occupiedWard, Ward contributingWard) {
		return occupiedWard.equals(contributingWard)?1f:transportFactor;
	}

}
