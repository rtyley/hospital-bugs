package com.hospitalbugs;

import uk.co.flamingpenguin.jewel.cli.CommandLineInterface;
import uk.co.flamingpenguin.jewel.cli.Option;

@CommandLineInterface(application = "hospital-bugs")
public interface CommandLineArgs {
	
	@Option(shortName="s", longName = "ward-stays", description="CSV file containg patient ward stays, one stay per line")
	String getWardStaysFile();
	
	@Option(shortName="i", longName = "infections", description="CSV file containg patient infections, one infection per line")
	String getInfectionsFile();

	@Option(longName = "lambda", description="Lambda factor by which load is reduced day-by-day")
	float getLambda();
	
	@Option(longName = "transport-constant", description="Constant Transport factor by which load is reduced from ward-to-ward")
	float getTransportConstant();
}
