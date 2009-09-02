import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.joda.time.DateTimeZone;

import com.hospitalbugs.analysis.ConstantTransportFunction;
import com.hospitalbugs.analysis.HospitalMicrobialLoad;
import com.hospitalbugs.analysis.HospitalMicrobialLoadCalculationFactory;
import com.hospitalbugs.analysis.PatientMicrobialLoadExposure;
import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.StandardisedMicrobialLoad;
import com.hospitalbugs.parser.CSVFileParser;
import com.hospitalbugs.parser.InfectionCSVLineParser;
import com.hospitalbugs.parser.PatientFactory;
import com.hospitalbugs.parser.PatientWardStayCSVLineParser;
import com.hospitalbugs.parser.WardFactory;


public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CSVFileParser csvFileParser = new CSVFileParser();
		DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/London");
		PatientFactory patientFactory = new PatientFactory();
		WardFactory wardFactory = new WardFactory();
		
		csvFileParser.parse(new FileReader(args[0]), new PatientWardStayCSVLineParser(patientFactory, wardFactory, dateTimeZone));
		List<Infection> infections = csvFileParser.parse(new FileReader(args[1]), new InfectionCSVLineParser(patientFactory, dateTimeZone));
		
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(infections);
		
		HospitalMicrobialLoad hospitalMicrobialLoad =
			new HospitalMicrobialLoadCalculationFactory().lambdaModel(donorOccupancy, 0.7f, new ConstantTransportFunction(0.2f));
		
		System.out.println(hospitalMicrobialLoad);
		PatientMicrobialLoadExposure exposure = new PatientMicrobialLoadExposure();
		for (Infection infection : infections) {
			StandardisedMicrobialLoad microbialLoad = exposure.microbialLoadExposureDuringPatientSusceptibilityFor(infection, hospitalMicrobialLoad);
			System.out.println(infection+" - "+microbialLoad);
		}
	}
}
