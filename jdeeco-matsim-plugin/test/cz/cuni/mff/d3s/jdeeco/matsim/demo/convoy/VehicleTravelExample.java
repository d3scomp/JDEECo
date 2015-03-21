package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

import java.io.File;
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
 * Example of vehicles traveling across the map
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class VehicleTravelExample {
	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		new VehicleTravelExample().testTravel();
	}

	@Test
	public void testTravel() throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		MATSimSimulation matSim = new MATSimSimulation(new File("input/config.xml"));

		// Create main application container
		DEECoSimulation realm = new DEECoSimulation(matSim.getTimer());
		
		// Add MATSim plug-in for all nodes
		realm.addPlugin(matSim);
		
		// Configure loop-back networking for all nodes
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);

		// Node hosting vehicle A
		MATSimVehicle agentA = new MATSimVehicle(0, 0); // MATSim agent with start position
		DEECoNode nodeA = realm.createNode(42, agentA); // DEECO node with Id and agent as plug-in
		Vehicle vehicleA = new Vehicle("Vehicle A", new CoordImpl(100000, 100000), agentA); // DEECO component controlling the vehicle
		nodeA.deployComponent(vehicleA);
		nodeA.deployEnsemble(OtherVehicleEnsemble.class);

		// Node hosting vehicle B
		MATSimVehicle agentB = new MATSimVehicle(0, 100000); // MATSim agent with start position
		DEECoNode nodeB = realm.createNode(45, agentB); // DEECO node with Id and agent as plug-in
		Vehicle vehicleB = new Vehicle("Vehicle B", new CoordImpl(0, 100000), agentB); // DEECO component controlling the vehicle
		nodeB.deployComponent(vehicleB);
		nodeB.deployEnsemble(OtherVehicleEnsemble.class);

		// Simulate for specified time
		realm.start(600000);

		// TODO: Check output
	}
}
