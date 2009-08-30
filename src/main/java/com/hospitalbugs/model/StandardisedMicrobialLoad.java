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
			float currentMicrobialLoadForInfection = added.get(infection);
			float additionalMicrobialLoadForInfection = loadEntry.getValue();
			added.put(infection, currentMicrobialLoadForInfection + additionalMicrobialLoadForInfection);
		}
		return new StandardisedMicrobialLoad(added);
	}

	public StandardisedMicrobialLoad addWithScalar(
			StandardisedMicrobialLoad additionalLoad, float scalar) {
		Map<Infection,Float> added = new HashMap<Infection, Float>(load);
		for (Map.Entry<Infection,Float> loadEntry : additionalLoad.load.entrySet()) {
			Infection infection = loadEntry.getKey();
			float currentMicrobialLoadForInfection = added.get(infection);
			float additionalMicrobialLoadForInfection = loadEntry.getValue() * scalar;
			added.put(infection, currentMicrobialLoadForInfection + additionalMicrobialLoadForInfection);
		}
		return new StandardisedMicrobialLoad(added);
	}
	
}
