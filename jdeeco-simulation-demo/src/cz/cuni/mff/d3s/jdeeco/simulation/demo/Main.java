package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Distribution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkBuilder;

/**
 * Main class for launching the FF scenario demo.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class Main {

	public static void main(String[] args) throws AnnotationProcessorException, FileNotFoundException {
		
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		RuntimeFrameworkBuilder builder = new RuntimeFrameworkBuilder(
				new RuntimeConfiguration(
						Scheduling.WALL_TIME, 
						Distribution.LOCAL, 
						Execution.SINGLE_THREADED));
		
		ConfigParser parser = new ConfigParser("component.cfg");
		
		Object component = null;
		List<RuntimeFramework> runtimes = new ArrayList<>();
		while ((component = parser.parseComponent()) != null) {
			RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
			processor.process(model, component, MemberDataAggregation.class); 
			
			RuntimeFramework runtime = builder.build(model); 
			runtimes.add(runtime);
			runtime.start();
		}		
	}
	
	private static class ConfigParser {
		BufferedReader in;
		ConfigParser(String filename) throws FileNotFoundException {
			in = new BufferedReader(new FileReader(filename));
		}
		public Object parseComponent() {
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
