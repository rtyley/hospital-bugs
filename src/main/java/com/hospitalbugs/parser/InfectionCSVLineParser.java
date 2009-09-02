package com.hospitalbugs.parser;

import static org.joda.time.format.ISODateTimeFormat.dateHourMinute;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;
import com.madgag.util.csv.CSVLineParser;

public class InfectionCSVLineParser implements CSVLineParser<Infection> {

	private final PatientFactory patientFactory;
	private DateTimeFormatter dateTimeFormat;

	public InfectionCSVLineParser(PatientFactory patientFactory, DateTimeZone dateTimeZone) {
		this.patientFactory = patientFactory;
		dateTimeFormat = dateHourMinute().withZone(dateTimeZone);
	}

	@Override
	public Infection parse(List<String> values) {
		Iterator<String> valueIterator = values.iterator();
		String patientId=valueIterator.next();
		String startText = valueIterator.next(),transitionText = valueIterator.next(),endText = valueIterator.next();
		
		Patient patient = patientFactory.get(patientId);
		return new Infection(patient, dateTimeFormat.parseDateTime(startText), dateTimeFormat.parseDateTime(transitionText), dateTimeFormat.parseDateTime(endText));
	}
	
}
