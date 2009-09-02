package com.hospitalbugs.parser;

import java.util.HashMap;
import java.util.Map;

import com.hospitalbugs.model.Ward;

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
