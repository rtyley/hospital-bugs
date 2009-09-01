package com.hospitalbugs.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class StandardisedMicrobialLoad {
	
	public static final StandardisedMicrobialLoad ZERO = new StandardisedMicrobialLoad(Collections.<Infection, Float>emptyMap());
	
	private final ImmutableMap<Infection,Float> load;
	
	private StandardisedMicrobialLoad(Map<Infection, Float> load) {
		this.load = ImmutableMap.copyOf(load);
	}
	
	public static StandardisedMicrobialLoad of(Map<Infection, Float> loadMap) {
		return new StandardisedMicrobialLoad(loadMap);
	}

	public StandardisedMicrobialLoad add(StandardisedMicrobialLoad additionalLoad) {
		Map<Infection,Float> added = new HashMap<Infection, Float>(load);
		for (Map.Entry<Infection,Float> loadEntry : additionalLoad.load.entrySet()) {
			Infection infection = loadEntry.getKey();
			float currentMicrobialLoadForInfection = nullIsZero(added.get(infection));
			float additionalMicrobialLoadForInfection = loadEntry.getValue();
			float newLoad = currentMicrobialLoadForInfection + additionalMicrobialLoadForInfection;
			added.put(infection, newLoad);
		}
		return new StandardisedMicrobialLoad(added);
	}

	public StandardisedMicrobialLoad addWithScalar(
			StandardisedMicrobialLoad additionalLoad, float scalar) {
		Map<Infection,Float> added = new HashMap<Infection, Float>(load);
		for (Map.Entry<Infection,Float> loadEntry : additionalLoad.load.entrySet()) {
			Infection infection = loadEntry.getKey();
			float currentMicrobialLoadForInfection = nullIsZero(added.get(infection));
			float additionalMicrobialLoadForInfection = loadEntry.getValue() * scalar;
			added.put(infection, currentMicrobialLoadForInfection + additionalMicrobialLoadForInfection);
		}
		return new StandardisedMicrobialLoad(added);
	}

	public float forInfection(Infection infection) {
		return nullIsZero(load.get(infection));
	}

	private static float nullIsZero(Float l) {
		return l==null?0:l;
	}
	
	
	
}
