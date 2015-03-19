package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

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
			InstantiationException, IllegalAccessException {
		new ConvoySimulationTest().testConvoy();
	}

	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		MATSimSimulation matSim = new MATSimSimulation("input/config.xml");
		
		/* create main application container */
 		DEECoSimulation realm = new DEECoSimulation(matSim.getTimer());
 		realm.addPlugin(matSim);
 		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
				
		// Node hosting vehicle A
		MATSimVehicle vehicleA = new MATSimVehicle(new CoordImpl(0, 0));
		DEECoNode nodeA = realm.createNode(42, vehicleA);
		Vehicle vehicleAComponent = new Vehicle("Vehicle A", new CoordImpl(100000, 100000), vehicleA); 
		nodeA.deployComponent(vehicleAComponent);
		
		// Node hosting vehicle B
		MATSimVehicle vehicleB = new MATSimVehicle(new CoordImpl(0, 100000));
		DEECoNode nodeB = realm.createNode(45, vehicleB);
		Vehicle vehicleBComponent = new Vehicle("Vehicle B", new CoordImpl(0, 100000), vehicleB); 
		nodeB.deployComponent(vehicleBComponent);
		
		
		// WHEN simulation is performed
		// TODO: TIME IS IGNORED
		realm.start(600000);

		// TODO: Check output
	}}
