package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.network.PacketReceiver;
import cz.cuni.mff.d3s.deeco.network.PacketSender;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
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
	static String OMNET_NETWORK_CONF_PATH = "network-config/network-demo.xml";
	
	static String DEFAULT_COMPONENT_CFG = "component.cfg";
	static String DEFAULT_SITE_CFG = "site.cfg";
	
	static int SIMULATION_DURATION = 10000;

	
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
		List<PositionAwareComponent> components = new LinkedList<>();
		while ((component = parser.parseComponent()) != null) {
			components.add(component);
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
			//Be careful here jDEECoAppModuleId != nodeId in omnet
			//As such when addressing directly (i.e when sending message to a concrete ip) you need to provide nodeId and not jDEECoAppModuleId.
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
		out.println(String.format("**.playgroundSizeX = %dm", (int) topRight.x));
		out.println(String.format("**.playgroundSizeY = %dm", (int) topRight.y));
		out.println();
		out.println(String.format("**.numNodes = %d", hosts.size()));
		out.println();
		out.println(omnetConfig.toString());
		
		StringBuilder routerConfig = generateNetworkConfig(areas, components, OMNET_NETWORK_CONF_PATH);
		
		out.println(routerConfig);
		
		out.close();

		logSimulationParameters(i);
		
		sim.run("Cmdenv", OMNET_CONFIG_PATH);
		
		//System.gc();
		System.out.println("Simulation finished.");
	}


	private static void logSimulationParameters(int componentCnt) {
		Log.d(String.format("Simulation parameters: %d components, packet size %d, publish period %d,"
				+ " %s publishing, boundary %s, cache deadline %d, cache wipe period %d, maxRebroadcastDelay %d",
				componentCnt, 
				Integer.getInteger(DeecoProperties.PACKET_SIZE, PacketSender.DEFAULT_PACKET_SIZE), 
				Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD, PublisherTask.DEFAULT_PUBLISHING_PERIOD), 
				Boolean.getBoolean(DeecoProperties.USE_INDIVIDUAL_KNOWLEDGE_PUBLISHING) ?  "individual" : "list",
				Boolean.getBoolean(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS) ?  "disabled" : "enabled",
				Integer.getInteger(DeecoProperties.MESSAGE_CACHE_DEADLINE, PacketReceiver.DEFAULT_MAX_MESSAGE_TIME),
				Integer.getInteger(DeecoProperties.MESSAGE_CACHE_WIPE_PERIOD, PacketReceiver.DEFAULT_MESSAGE_WIPE_PERIOD),
				Integer.getInteger(DeecoProperties.MAXIMUM_REBROADCAST_DELAY, KnowledgeDataManager.DEFAULT_MAX_REBROADCAST_DELAY)));
	}
	
	private static StringBuilder generateNetworkConfig(Set<Area> areas, List<PositionAwareComponent> components, String networkConfig) throws IOException {
		assert areas.size() < 250;
		StringBuilder nConfig = new StringBuilder();
		StringBuilder oConfig = new StringBuilder();
		oConfig.append("\n");
		oConfig.append("**.numRouters = " + areas.size() + "\n\n");
		oConfig.append("\n");
		
		nConfig.append("<config>\n");
		//Generate interfaces for each of the areas
		List<PositionAwareComponent> copyOfComponents = new LinkedList<>(components);
		Set<PositionAwareComponent> toDelete;
		PositionAwareComponent c;
		int i = 0;
		for (Area area : areas) {
			nConfig.append("<interface hosts='router["+ i +"]' names='wlan*' address='192." + (i+1) + ".x.x' netmask='255.255.x.x'/>\n");
			//Default route to Wireless
			nConfig.append("<route hosts='router["+ i +"]' destination='192." + (i+1) + ".0.0' netmask='255.255.0.0' interface='wlan0'/>\n");
			//Default route to previous router
			if (i > 0)
				nConfig.append("<route hosts='router["+ i +"]' destination='192.0.0.0' netmask='255.0.0.0' gateway='router["+ (i-1) +"]' interface='eth0'/>\n");
			toDelete = new HashSet<>();
			for (int j = 0; j < copyOfComponents.size(); j++) {
				c = copyOfComponents.get(j);
				if (area.isInArea(c.position)) {
					toDelete.add(c);
					//Interface description
					nConfig.append("<interface hosts='node["+ j +"]' names='nic80211' address='192." + (i+1) + ".x.x' netmask='255.255.x.x'/>\n");
					//Default route
					nConfig.append("<route hosts='node[" + j + "]' destination='*' netmask='*' gateway='router[" + i + "]' interface='nic80211'/>\n");
				}
			}
			nConfig.append("\n");
			copyOfComponents.removeAll(toDelete);
			
			oConfig.append("**.router["+i+"].mobility.initialX = " + area.getCenterX() + "m\n");
			oConfig.append("**.router["+i+"].mobility.initialY = " + area.getCenterY() + "m\n");
			oConfig.append("**.router["+i+"].mobility.initialZ = 0m\n");
			
			i++;
		}
		nConfig.append("</config>");
		PrintWriter out = new PrintWriter(Files.newOutputStream(Paths.get(networkConfig), StandardOpenOption.CREATE));
		out.println(nConfig.toString());	
		out.close();
		return oConfig;
	}
}
