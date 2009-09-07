package com.hospitalbugs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.joda.time.DateTimeZone;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

import com.hospitalbugs.analysis.ConstantTransportFunction;
import com.hospitalbugs.analysis.HospitalMicrobialLoad;
import com.hospitalbugs.analysis.HospitalMicrobialLoadCalculationFactory;
import com.hospitalbugs.analysis.PatientMicrobialLoadExposure;
import com.hospitalbugs.io.InfectionCSVLineParser;
import com.hospitalbugs.io.MicrobialLoadExposureReportWriter;
import com.hospitalbugs.io.PatientWardStayCSVLineParser;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.PatientFactory;
import com.hospitalbugs.model.WardFactory;
import com.madgag.io.csv.CSVFileParser;

public class Main {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ArgumentValidationException {
		
		CommandLineArgs parsedArgs;
		try {
			parsedArgs = CliFactory.parseArguments(CommandLineArgs.class, args);
		} catch (ArgumentValidationException e) {
			System.err.println(e.getMessage());
			return;
		}

		CSVFileParser csvFileParser = new CSVFileParser();
		DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/London");
		PatientFactory patientFactory = new PatientFactory();
		WardFactory wardFactory = new WardFactory();

		csvFileParser.parse(new FileReader(parsedArgs.getWardStaysFile()),
				new PatientWardStayCSVLineParser(patientFactory, wardFactory,
						dateTimeZone));
		List<Infection> infections = csvFileParser.parse(
				new FileReader(parsedArgs.getInfectionsFile()), new InfectionCSVLineParser(
						patientFactory, dateTimeZone));

		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(
				infections);

		HospitalMicrobialLoad hospitalMicrobialLoad = new HospitalMicrobialLoadCalculationFactory()
				.lambdaModel(donorOccupancy, parsedArgs.getLambda(),	new ConstantTransportFunction(parsedArgs.getTransport()));
		
		OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
		MicrobialLoadExposureReportWriter reportWriter = new MicrobialLoadExposureReportWriter(outputWriter,new PatientMicrobialLoadExposure());
		reportWriter.write(infections, hospitalMicrobialLoad);
		outputWriter.flush();
	}
}
