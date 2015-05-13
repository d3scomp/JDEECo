package cz.cuni.mff.d3s.jdeeco.matsimomnet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.matsim.plugin.MATSimVehicle;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Example of vehicles traveling across the map
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class VehicleTravelTest {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		new VehicleTravelTest().travelMobilityLimitedRangeOmnetRunner(false);
	}

	@Test
	public void travelMobilityLimitedRangeOmnetTest() throws AnnotationProcessorException, InterruptedException, DEECoException,
		InstantiationException, IllegalAccessException, IOException {
		travelMobilityLimitedRangeOmnetRunner(true);
	}
	public void travelMobilityLimitedRangeOmnetRunner(boolean silent) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}
		
		// Create joint OMNeT-MATSim simulation plug-in
		OMNeTMATSimSimulation omnetmatsim = new OMNeTMATSimSimulation("input/config.xml");
				
		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnetmatsim);
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(omnetmatsim);
		
		// Node hosting vehicle A
		MATSimVehicle agentA = new MATSimVehicle(); // MATSim agent with start position
		DEECoNode nodeA = simulation.createNode(42, agentA, new OMNeTBroadcastDevice(), new PositionPlugin(0, 0)); // DEECO node with Id and agent as plug-in
		Vehicle vehicleA = new Vehicle("Vehicle A", new CoordImpl(1000, 2000), agentA, outputStream); // DEECO component controlling the vehicle
		nodeA.deployComponent(vehicleA);
		nodeA.deployEnsemble(OtherVehicleEnsemble.class);

		// Node hosting vehicle B
		MATSimVehicle agentB = new MATSimVehicle(); // MATSim agent with start position
		DEECoNode nodeB = simulation.createNode(45, agentB, new OMNeTBroadcastDevice(), new PositionPlugin(2000, 0)); // DEECO node with Id and agent as plug-in
		Vehicle vehicleB = new Vehicle("Vehicle B", new CoordImpl(1000, 2000), agentB, outputStream); // DEECO component controlling the vehicle
		nodeB.deployComponent(vehicleB);
		nodeB.deployEnsemble(OtherVehicleEnsemble.class);

		// Simulate for specified time
		simulation.start(10 * 60000);

		if(silent) {
			// Check if the vehicle initially have not known about each other as they were close
			assertThat(baos.toString(), containsString("Vehicle A, pos: 5407 (400, 1105), dst: 4215, speed: , otherPos: UNKNOWN"));
			assertThat(baos.toString(), containsString("Vehicle B, pos: 6187 (800, 1105), dst: 4215, speed: , otherPos: UNKNOWN"));
			
			// Check both cars reached the destination and know about each other (as they are close to each other)
			assertThat(baos.toString(), containsString("Vehicle A, pos: 4215 (995, 2000), dst: 4215, speed: , otherPos: 4215 (995, 2000)"));
			assertThat(baos.toString(), containsString("Vehicle B, pos: 4215 (995, 2000), dst: 4215, speed: , otherPos: 4215 (995, 2000)"));
		}
	}
}
