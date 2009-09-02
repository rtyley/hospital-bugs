package com.hospitalbugs.analysis;

import static org.joda.time.Duration.standardHours;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

import com.hospitalbugs.model.HospitalInfectionDonorOccupancy;
import com.hospitalbugs.model.Infection;
import com.hospitalbugs.model.InfectionBuilder;
import com.hospitalbugs.model.Patient;
import com.hospitalbugs.model.PatientBuilder;
import com.hospitalbugs.model.Ward;
import com.hospitalbugs.model.WardBuilder;

public class RandomHospitalInfectionDataFactory {

	private static Random random = new Random();
	
	public static HospitalInfectionDonorOccupancy generate(Interval fullTimeSpan, int numInfections, int numWards) {
		List<Ward> wards = generateWards(numWards);
		List<Infection> infections = generateInfectedPatientsOnWards(wards, numInfections,fullTimeSpan);
		
		
		HospitalInfectionDonorOccupancy donorOccupancy = new HospitalInfectionDonorOccupancy(infections);
		return donorOccupancy;
	} 

	private static List<Infection> generateInfectedPatientsOnWards(List<Ward> wards, int numInfections,Interval fullTimeSpan) {
		List<Infection> infections = new ArrayList<Infection>(numInfections);
		for (int p=0;p<numInfections;++p) {
			PatientBuilder patientBuilder = new PatientBuilder();
			int numWardStays=1+random.nextInt(3);
			ReadableInstant wardStayStart = fullTimeSpan.getStart().plus(1l+(long)(random.nextFloat() * fullTimeSpan.toDurationMillis())).toInstant();
			Interval infectious = new Interval(new Instant(wardStayStart).plus(standardHours(random.nextInt(10))), standardHours(10+random.nextInt(150)));
			for (int i=0;i<numWardStays;++i) {
				Interval stay = new Interval(wardStayStart, standardHours(random.nextInt(100)));
				
				patientBuilder.wardStay(randomWard(wards), stay);
				wardStayStart = stay.getEnd();
			}
			Patient patient = patientBuilder.toPatient();
			Infection infection = new InfectionBuilder().patient(patient).susceptabilityAndInfectiousness(standardHours(10+random.nextInt(100)),infectious).toInfection();
			infections.add(infection);
		}
		return infections;
	}

	private static Ward randomWard(List<Ward> wards) {
		return wards.get(random.nextInt(wards.size()));
	}

	private static List<Ward> generateWards(int numWards) {
		List<Ward> wards = new ArrayList<Ward>(numWards);
		for (int w=0;w<numWards;++w) {
			wards.add(new WardBuilder().toWard());
		}
		return wards;
	}
	
}
