package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimSimulation;
import cz.cuni.mff.d3s.jdeeco.matsim.MATSimVehicle;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class ConvoySimulationTest {

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		new ConvoySimulationTest().testConvoy();
	}

	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException, IOException {
		MATSimSimulation matSim = new MATSimSimulation("maps/grid.xml");
		
		/* create main application container */
 		DEECoSimulation realm = new DEECoSimulation(matSim.getTimer());
 		realm.addPlugin(matSim);
 		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
				
		// Node hosting vehicle A
		MATSimVehicle agentA = new MATSimVehicle(new CoordImpl(0, 0));
		DEECoNode nodeA = realm.createNode(42, agentA);
		Vehicle vehicleA = new Vehicle("Vehicle A", new CoordImpl(100000, 100000), agentA); 
		nodeA.deployComponent(vehicleA);
		
		// Node hosting vehicle B
		MATSimVehicle agentB = new MATSimVehicle(new CoordImpl(0, 100000));
		DEECoNode nodeB = realm.createNode(45, agentB);
		Vehicle vehicleB = new Vehicle("Vehicle B", new CoordImpl(0, 100000), agentB); 
		nodeB.deployComponent(vehicleB);
		
		
		// Simulate for specified time
		realm.start(600000);

		// TODO: Check output
	}}
