package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;

/**
 * Main class for launching the FF scenario demo.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class Main {

	static double POSITION_FACTOR = 100;
	static int PACKET_SIZE = 1000;
	static int PUBLISHING_PERIOD = 500;
	
	static String CONFIG_TEMPLATE = "omnetpp.ini.templ";
	static String CONFIG_PATH = "omnetpp.ini";

	
	public static void main(String[] args) throws AnnotationProcessorException, IOException {
		
		Simulation sim = new Simulation();
		
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		SimulationRuntimeBuilder builder = new SimulationRuntimeBuilder();
		
		ConfigParser parser = new ConfigParser("component.cfg");
		
		PositionAwareComponent component = null;
		List<RuntimeFramework> runtimes = new ArrayList<>();
		List<Host> hosts = new ArrayList<>();

		
		StringBuilder omnetConfig = new StringBuilder();
				
		while ((component = parser.parseComponent()) != null) {
			RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
			processor.process(model, component, MemberDataAggregation.class); 
						
			omnetConfig.append(String.format(
					"#**.node[%s].mobility.initialX = %dm\n", 
					component.id, component.position.longitude * POSITION_FACTOR));
			omnetConfig.append(String.format(
					"#**.node[%s].mobility.initialY = %dm\n", 
					component.id, component.position.latitude * POSITION_FACTOR));
			omnetConfig.append(String.format(
					"#**.node[%s].mobility.initialZ = 0m\n", component.id));
			
			Host host = sim.getHost(component.id, PACKET_SIZE);			
			hosts.add(host);
			
			// there is only one component instance
			model.getComponentInstances().get(0).getInternalData().put(PositionAwareComponent.HOST_REFERENCE, host);
			
			RuntimeFramework runtime = builder.build(host, model, PUBLISHING_PERIOD); 
			runtimes.add(runtime);			
		}	
		
		Files.copy(Paths.get(CONFIG_TEMPLATE), Paths.get(CONFIG_PATH), StandardCopyOption.REPLACE_EXISTING);
		
		//sim.initialize(); //loads Library
		
		//sim.run("Cmdenv");
		
		System.gc();
		System.out.println("Simulation finished.");
	}
	
	private static class ConfigParser {
		BufferedReader in;
		ConfigParser(String filename) throws FileNotFoundException {
			in = new BufferedReader(new FileReader(filename));
		}
		public PositionAwareComponent parseComponent() {
			if (in == null)
				return null;
			String line;
			try {
				line = in.readLine();
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			}
			if (line == null)
				return null;
			
			String[] parts = line.split(" ");
			if (parts.length < 4)
				return null;
			
			
			switch (parts[0]) {
			case "M":
				if (parts.length < 5)
					return null;
				return new Member(parts[1], parts[2], 
						new Position(Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
			case "L":
				if (parts.length < 5)
					return null;
				return new Leader(parts[1], parts[2], 
						new Position(Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
			case "O":				
				return new OtherComponent(parts[1], 
						new Position(Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
			default:
				return null;
			}			
		}
	}
}
