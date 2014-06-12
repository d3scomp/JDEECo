package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.DirectGossipStrategy;
import cz.cuni.mff.d3s.deeco.network.DirectRecipientSelector;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.network.PacketReceiver;
import cz.cuni.mff.d3s.deeco.network.PacketSender;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.SimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulation;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulationRuntimeBuilder;

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
	
	static String DEFAULT_COMPONENT_CFG = "configurations/component.cfg";
	static String DEFAULT_SITE_CFG = "configurations/site.cfg";
	
	static int SIMULATION_DURATION = 60000;
	static int IP_GOSSIP_TARGET_PERCENTAGE = 20;
	

	
	public static void main(String[] args) throws AnnotationProcessorException, IOException {
		String componentCfg = DEFAULT_COMPONENT_CFG;
		String siteCfg = DEFAULT_SITE_CFG;
		
		if (args.length >= 2) {
			componentCfg = args[0];
			siteCfg = args[1];
		}
		
		OMNetSimulation sim = new OMNetSimulation();
		
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		OMNetSimulationRuntimeBuilder builder = new OMNetSimulationRuntimeBuilder();
		
		SiteConfigParser siteParser = new SiteConfigParser(siteCfg);
		Position topRight = siteParser.parseTopRightCorner();
		
		Area area = null;
		Set<Area> areas = new HashSet<>();
		while ((area = siteParser.parseArea()) != null) {
			areas.add(area);
		}
		
		AreaNetworkRegistry.INSTANCE.initialize(areas);
		
		final AreaBasedSelector directRecipientSelector = new AreaBasedSelector();
		directRecipientSelector.initialize(AreaNetworkRegistry.INSTANCE);

		final Random rnd = new Random(areas.size());
		final DirectGossipStrategy directGossipStrategy = new DirectGossipStrategy() {
			@Override
			public Collection<String> filterRecipients(
					Collection<String> recipients) {
				
				if (recipients.isEmpty())
					return recipients;
												
 				int targetCnt = 3;//(int) Math.ceil(recipients.size() * (IP_GOSSIP_TARGET_PERCENTAGE/100.0)); 
 				targetCnt = Math.min(recipients.size(), targetCnt);				
				List<String> copy = new ArrayList<>(recipients);
				
				// shuffle 'copy' and select the first 'targetCnt' elements
				for(int i=0; i < targetCnt; i++){
			        int pos = i + rnd.nextInt(copy.size() - i);			      
			        String rId = copy.get(pos);
			        copy.set(pos, copy.get(i));
			        copy.set(i, rId);			        
			    }
				return copy.subList(0, targetCnt);
				
				// originally was this				
				//for (String rId: recipients) {
				//	//20% chances of sending to the given recipient
				//	if (rnd.nextInt(100) < 20)
				//		result.add(rId);
				//}
			}
		};	
		
		// for each component config crate a separate model including only the component and all ensemble definitions,
		// a separate host, and a separate runtime framework
		List<PositionAwareComponent> components = new LinkedList<>();
		PositionAwareComponent component = null;
		List<RuntimeFramework> runtimes = new ArrayList<>();
		List<SimulationHost> hosts = new ArrayList<>();		
		StringBuilder omnetConfig = new StringBuilder();
		ComponentConfigParser parser = new ComponentConfigParser(componentCfg);
		int i = 0;
		
		while ((component = parser.parseComponent()) != null) {
			components.add(component);
			RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
			processor.process(model, component, MemberDataAggregation.class); 
						
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialX = %dm %n", 
					i, (int) (component.position.x)));			
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialY = %dm %n", 
					i, (int) (component.position.y)));
			omnetConfig.append(String.format(
					"**.node[%s].mobility.initialZ = 0m %n", i));
			omnetConfig.append(String.format(
					"**.node[%s].appl.id = \"%s\" %n%n", i, component.id));
			SimulationHost host = sim.getHost(component.id, "node["+i+"]");			
			hosts.add(host);
			
			AreaNetworkRegistry.INSTANCE.addComponent(component);
			
			// there is only one component instance
			for (ComponentInstance ci: model.getComponentInstances()) {
				PositionAwareComponent.initialize(ci, new PositionSensor(host, sim));
			}
			Collection<DirectRecipientSelector> recipientSelectors = null;
			if (component.hasIP) {
				recipientSelectors = Arrays.asList((DirectRecipientSelector) directRecipientSelector);
			}
			RuntimeFramework runtime = builder.build(host, sim, model, recipientSelectors, directGossipStrategy); 
			runtimes.add(runtime);
			runtime.start();
			i++;
		}		
		
		int seed = omnetConfig.toString().hashCode();
		System.out.println(String.format("Using Gossip Strategy seed %d.", seed));
		rnd.setSeed(seed);
		
		String confName = "omnetpp";
		if (args.length >= 3) {
			confName = args[2];
		}
		String confFile = confName + ".ini";
		Scanner scanner = new Scanner(new File(OMNET_CONFIG_TEMPLATE));
		String template = scanner.useDelimiter("\\Z").next();
		template = template.replace("<<<configName>>>", confName);
		scanner.close();
		
		PrintWriter out = new PrintWriter(
				Files.newOutputStream(Paths.get(confFile), 
						StandardOpenOption.CREATE, 
						StandardOpenOption.WRITE, 
						StandardOpenOption.TRUNCATE_EXISTING));
		out.println(template);
		out.println();
		out.println(String.format("**.playgroundSizeX = %dm", (int) topRight.x));
		out.println(String.format("**.playgroundSizeY = %dm", (int) topRight.y));
		out.println();
		out.println(String.format("**.numNodes = %d", hosts.size()));
		out.println(); 
		//out.println(String.format("**.node[*].appl.packet802154ByteLength = %dB", Integer.getInteger(DeecoProperties.PACKET_SIZE, PacketSender.DEFAULT_PACKET_SIZE)));
		out.println("**.node[*].appl.packet802154ByteLength = 128B");
		out.println();
		out.println(); 
		out.println(String.format("sim-time-limit = %ds", SIMULATION_DURATION / 1000));
		out.println();
		out.println(omnetConfig.toString());
		
//		StringBuilder routerConfig = generateNetworkConfig(areas, components, OMNET_NETWORK_CONF_PATH);
		
//		out.println(routerConfig);
		
		out.close();

		logSimulationParameters(i);
		
		sim.run("Cmdenv", confFile);
		
		sim.finalize();
		
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
	
}
