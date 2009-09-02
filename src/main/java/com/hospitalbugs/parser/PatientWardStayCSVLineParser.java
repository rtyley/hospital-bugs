package com.hospitalbugs.parser;

import static org.joda.time.format.ISODateTimeFormat.dateHourMinute;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;

import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.Ward;
import com.madgag.util.csv.CSVLineParser;

public class PatientWardStayCSVLineParser implements CSVLineParser<Patient> {

	private final PatientFactory patientFactory;
	private final WardFactory wardFactory;
	private DateTimeFormatter dateTimeFormat;

	public PatientWardStayCSVLineParser(PatientFactory patientFactory,	WardFactory wardFactory, DateTimeZone dateTimeZone) {
		this.patientFactory = patientFactory;
		this.wardFactory = wardFactory;
		dateTimeFormat = dateHourMinute().withZone(dateTimeZone);
	}

	@Override
	public Patient parse(List<String> values) {
		Iterator<String> valueIterator = values.iterator();
		String patientId=valueIterator.next();
		String wardCode=valueIterator.next();
		String wardStayStartText = valueIterator.next(), wardStayEndTextString = valueIterator.next();
		
		Patient patient = patientFactory.get(patientId);
		Ward ward = wardFactory.get(wardCode);
		Interval wardStayInterval = new Interval(dateTimeFormat.parseDateTime(wardStayStartText),dateTimeFormat.parseDateTime(wardStayEndTextString));
		
		patient.addWardStay(ward, wardStayInterval);
		return patient;
	}
	
}
