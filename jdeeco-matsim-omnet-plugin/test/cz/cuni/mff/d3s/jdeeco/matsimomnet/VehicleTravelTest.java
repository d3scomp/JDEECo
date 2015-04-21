package cz.cuni.mff.d3s.jdeeco.matsimomnet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.matsim.plugin.MATSimVehicle;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.position.PositionAware;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Example of vehicles traveling across the map
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class VehicleTravelTest {
	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		new VehicleTravelTest().testTravel();
	}

	@Test @Ignore("We currently cannot run multiple OMNeT based simulations AND this is not yet ready to work properly")
	public void testTravel() throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		OMNeTSimulation omnet = new OMNeTSimulation();
				
		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(omnet);
		simulation.addPlugin(new MATSimWithOMNeTSimulation("input/config.xml"));
		
		// Node hosting vehicle A
		MATSimVehicle agentA = new MATSimVehicle(); // MATSim agent with start position
		DEECoNode nodeA = simulation.createNode(42, agentA, new PositionAware(0, 0)); // DEECO node with Id and agent as plug-in
		Vehicle vehicleA = new Vehicle("Vehicle A", new CoordImpl(100000, 100000), agentA); // DEECO component controlling the vehicle
		nodeA.deployComponent(vehicleA);
		nodeA.deployEnsemble(OtherVehicleEnsemble.class);

		// Node hosting vehicle B
		MATSimVehicle agentB = new MATSimVehicle(); // MATSim agent with start position
		DEECoNode nodeB = simulation.createNode(45, agentB, new PositionAware(0, 100000)); // DEECO node with Id and agent as plug-in
		Vehicle vehicleB = new Vehicle("Vehicle B", new CoordImpl(100000, 100000), agentB); // DEECO component controlling the vehicle
		nodeB.deployComponent(vehicleB);
		nodeB.deployEnsemble(OtherVehicleEnsemble.class);

		// Simulate for specified time
		simulation.start(7 * 60000);

		// Check both cars reached the destination and know about each other
		assertThat(log.getLog(), containsString("Vehicle A, pos: 4410 (1995, 2000), dst: 4410, speed: , otherPos: 4410 (1995, 2000)"));
		assertThat(log.getLog(), containsString("Vehicle B, pos: 4410 (1995, 2000), dst: 4410, speed: , otherPos: 4410 (1995, 2000)"));
	}
}
