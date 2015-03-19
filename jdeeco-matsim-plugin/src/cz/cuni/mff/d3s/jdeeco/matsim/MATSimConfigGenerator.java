package cz.cuni.mff.d3s.jdeeco.matsim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MATSimConfigGenerator {
	public static String getContent(String map) {
		StringBuilder content = new StringBuilder();

		content.append(String.format("<?xml version='1.0' encoding='UTF-8'?>%n"));
		content.append(String.format("<!DOCTYPE config SYSTEM \"./dtd/config_v1.dtd\">%n"));

		content.append(String.format("<config>%n"));

		// Network / map module
		content.append(String.format("<module name=\"network\">%n"));
		content.append(String.format("<param name=\"inputNetworkFile\" value=\"%s\" />%n", map));
		content.append(String.format("</module>%n"));

		// Controller module
		content.append(String.format("<module name=\"controler\">%n"));
		content.append(String.format("<param name=\"writeEventsInterval\" value=\"1000\" />%n"));
		content.append(String.format("<param name=\"writePlansInterval\" value=\"1000\" />%n"));
		content.append(String.format("<param name=\"eventsFileFormat\" value=\"xml\" />%n"));
		content.append(String.format("<param name=\"outputDirectory\" value=\"matsim\"/>%n"));
		content.append(String.format("<param name=\"firstIteration\" value=\"0\" />%n"));
		content.append(String.format("<param name=\"lastIteration\" value=\"0\" />%n"));
		content.append(String.format("<param name=\"mobsim\" value=\"qsim\" />%n"));
		content.append(String.format("</module>%n"));

		// QSim module
		content.append(String.format("<module name=\"qsim\" >%n"));
		content.append(String.format("<param name=\"startTime\" value=\"00:00:00\" />%n"));
		content.append(String.format("<param name=\"endTime\" value=\"00:00:00\" />%n"));
		content.append(String.format("<param name=\"flowCapacityFactor\" value=\"1.00\" />%n"));
		content.append(String.format("<param name=\"storageCapacityFactor\" value=\"1.00\" />%n"));
		content.append(String.format("<param name=\"numberOfThreads\" value=\"1\" />%n"));
		content.append(String.format("<param name=\"snapshotperiod\" value = \"00:00:01\"/>%n"));
		content.append(String.format("<param name=\"simStarttimeInterpretation\" value = \"onlyUseStarttime\"/>%n"));
		content.append(String.format("<param name=\"removeStuckVehicles\" value=\"false\" />%n"));
		content.append(String.format("<param name=\"stuckTime\" value=\"3600.0\" />%n"));
		content.append(String.format("<param name=\"timeStepSize\" value=\"00:00:0.2\" />%n"));
		content.append(String.format("<param name=\"trafficDynamics\" value=\"queue\" />%n"));
		content.append(String.format("</module>%n"));

		content.append(String.format("</config>%n"));
		
		return content.toString();
	}

	public static File writeToTemp(String map) throws IOException {
		File temp = File.createTempFile("config", ".xml");
		temp.deleteOnExit();

		FileWriter writer = new FileWriter(temp);
		writer.write(getContent(map));
		writer.close();

		return temp;
	}
}
