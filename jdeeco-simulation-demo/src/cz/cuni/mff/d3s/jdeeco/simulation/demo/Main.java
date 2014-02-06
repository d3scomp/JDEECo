package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;

/**
 * Main class for launching the CBSE evaluation demo.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class Main {

	static String OMNET_CONFIG_TEMPLATE = "omnetpp.ini.templ";
	static String OMNET_CONFIG_PATH = "omnetpp.ini";
	
	static String DEFAULT_COMPONENT_CFG = "component.cfg";
	static String DEFAULT_SITE_CFG = "site.cfg";

	
	public static void main(String[] args) throws AnnotationProcessorException, IOException {
		String componentCfg = DEFAULT_COMPONENT_CFG;
		String siteCfg = DEFAULT_SITE_CFG;
		
		if (args.length == 2) {
			componentCfg = args[0];
			siteCfg = args[1];
		}
		
		Simulation sim = new Simulation();
		sim.initialize(); //loads Library
		
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		SimulationRuntimeBuilder builder = new SimulationRuntimeBuilder();
		
		SiteConfigParser siteParser = new SiteConfigParser(siteCfg);
		Position topRight = siteParser.parseTopRightCorner();
		
		Area area = null;
		Set<Area> areas = new HashSet<>();
		while ((area = siteParser.parseArea()) != null) {
			areas.add(area);
		}
		TeamLocationService.INSTANCE.init(areas);
		
		ComponentConfigParser parser = new ComponentConfigParser(componentCfg);
		
		PositionAwareComponent component = null;
		List<RuntimeFramework> runtimes = new ArrayList<>();
		List<Host> hosts = new ArrayList<>();
		
		StringBuilder omnetConfig = new StringBuilder();
		int i = 0;		
		
		// for each component config crate a separate model including only the component and all ensemble definitions,
		// a separate host, and a separate runtime framework
		while ((component = parser.parseComponent()) != null) {
			RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
			processor.process(model, component, MemberDataAggregation.class); 
						
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialX = %dm\n", 
					i, (int) (component.position.x)));
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialY = %dm\n", 
					i, (int) (component.position.y)));
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialZ = 0m\n", i));
			omnetConfig.append(String.format(
					"**.node[%s].appl.id = \"%s\"\n\n", i, component.id));
			
			Host host = sim.getHost(component.id);			
			hosts.add(host);
			
			// there is only one component instance
			model.getComponentInstances().get(0).getInternalData().put(PositionAwareComponent.HOST_REFERENCE, host);
			
			RuntimeFramework runtime = builder.build(host, model); 
			runtimes.add(runtime);
			runtime.start();
			i++;
		}	
		
		Files.copy(Paths.get(OMNET_CONFIG_TEMPLATE), Paths.get(OMNET_CONFIG_PATH), StandardCopyOption.REPLACE_EXISTING);
		
		PrintWriter out = new PrintWriter(Files.newOutputStream(Paths.get(OMNET_CONFIG_PATH), StandardOpenOption.APPEND));
		out.println();
		out.println(String.format("**.playgroundWidth = %dm", (int) topRight.x));
		out.println(String.format("**.playgroundHeight = %dm",(int) topRight.y));
		out.println(String.format("**.playgroundSizeX = %dm", (int) topRight.x));
		out.println(String.format("**.playgroundSizeY = %dm", (int) topRight.y));
		out.println();
		out.println(String.format("**.numNodes = %d", hosts.size()));
		out.println();
		out.println(omnetConfig.toString());		
		out.close();
		
		sim.run("Cmdenv", OMNET_CONFIG_PATH);
		
		//System.gc();
		System.out.println("Simulation finished.");
	}
}
