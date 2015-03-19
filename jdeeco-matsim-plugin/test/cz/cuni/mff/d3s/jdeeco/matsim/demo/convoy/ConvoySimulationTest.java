package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.matsim.api.core.v01.Id;
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

		MATSimSimulation simulation = new MATSimSimulation("input/config.xml");
		
		/* create main application container */
 		DEECoSimulation realm = new DEECoSimulation(simulation.getTimer());
 		realm.addPlugin(simulation);
 		
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		CoordImpl vehicle0StartPos = new CoordImpl(0, 0);
		CoordImpl vehicle0EndPos = new CoordImpl(100000, 100000);
		
		MATSimVehicle vehicle0Plug = new MATSimVehicle(vehicle0StartPos);
		
		Id vehicle0StartLinkId = simulation.getRouter().findNearestLink(vehicle0StartPos).getId();
		Id vehicle0EndLinkId = simulation.getRouter().findNearestLink(vehicle0EndPos).getId();
		
		DEECoNode vehicle0 = realm.createNode(42, vehicle0Plug);
		vehicle0.deployComponent(new Vehicle(String.valueOf(42), vehicle0EndLinkId, vehicle0StartLinkId, vehicle0Plug.getActuatorProvider(), vehicle0Plug.getSensorProvider(), simulation.getRouter(), simulation.getTimer()));
		
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(0);
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);
		
		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(1);
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		// TODO: TIME IS IGNORED
		realm.start(600000);

		// THEN the follower prints out the following (as there is no network and the components cannot exchange data)
		assertThat(log.getLog(), containsString("Follower F: me = (1,3) leader = Leader"));
	}}
