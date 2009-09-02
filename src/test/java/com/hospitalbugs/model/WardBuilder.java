package com.hospitalbugs.model;

public class WardBuilder {

	private static int defaultWardId = 0;
	
	public Ward toWard() {
		return new Ward(newWardCode());
	}

	private static String newWardCode() {
		return "W"+(defaultWardId++);
	}

}
