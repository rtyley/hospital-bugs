package com.hospitalbugs.model;

import java.util.HashMap;
import java.util.Map;


public class WardFactory {

	Map<String,Ward> wards = new HashMap<String,Ward>();
	
	
	public Ward get(String wardCode) {
		Ward ward = wards.get(wardCode);
		if (ward==null) {
			wards.put(wardCode, ward = new Ward(wardCode));
		}
		return ward;
	}

}
