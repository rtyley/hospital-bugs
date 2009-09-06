package com.hospitalbugs.io;

import static org.joda.time.format.ISODateTimeFormat.dateHourMinute;

import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.PatientFactory;
import com.madgag.util.csv.CSVLineParser;

public class InfectionCSVLineParser implements CSVLineParser<Infection> {

	private final PatientFactory patientFactory;
	private DateTimeFormatter dtFormat;

	public InfectionCSVLineParser(PatientFactory patientFactory, DateTimeZone dateTimeZone) {
		this.patientFactory = patientFactory;
		dtFormat = dateHourMinute().withZone(dateTimeZone);
	}

	@Override
	public Infection parse(List<String> values) {
		Iterator<String> valueIterator = values.iterator();
		String infectionId=valueIterator.next();
		String patientId=valueIterator.next();
		String startText = valueIterator.next(),transitionText = valueIterator.next(),endText = valueIterator.next();
		
		Patient patient = patientFactory.get(patientId);
		return new Infection(infectionId, patient, dtFormat.parseDateTime(startText), dtFormat.parseDateTime(transitionText), dtFormat.parseDateTime(endText));
	}
	
}
