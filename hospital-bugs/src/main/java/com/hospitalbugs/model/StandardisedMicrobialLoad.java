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

	public static StandardisedMicrobialLoad loadOf(Infection infection,float load) {
		return new StandardisedMicrobialLoad(ImmutableMap.of(infection, load));
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
	
	public ImmutableMap<Infection, Float> getLoad() {
		return load;
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+load.toString()+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((load == null) ? 0 : load.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandardisedMicrobialLoad other = (StandardisedMicrobialLoad) obj;
		if (load == null) {
			if (other.load != null)
				return false;
		} else if (!load.equals(other.load))
			return false;
		return true;
	}

	public StandardisedMicrobialLoad remove(Infection infection) {
		Map<Infection,Float> moo = new HashMap<Infection, Float>(load);
		moo.remove(infection);
		return new StandardisedMicrobialLoad(moo);
	}
	
}
