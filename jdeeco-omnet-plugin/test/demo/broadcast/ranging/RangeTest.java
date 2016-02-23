package demo.broadcast.ranging;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNeTUtils;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;
import demo.broadcast.DataPass;
import demo.broadcast.Seeder;

/**
 * Convoy demo with broadcast networking provided by OMNeT
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RangeTest {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException {

		final PrintStream outputStream = System.out;
		
		OMNeTSimulation omnet = new OMNeTSimulation();
		omnet.set80154txPower(OMNeTUtils.RangeToPower_802_15_4(123));

		// Create main application container
		DEECoSimulation simulation = new DEECoSimulation(omnet.getTimer());
		simulation.addPlugin(Network.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(omnet);
		
		// Deploy components and ensembles
		DEECoNode node0 = simulation.createNode(new OMNeTBroadcastDevice(), new PositionPlugin(0, 0));
		node0.deployComponent(new Seeder("S", outputStream, omnet.getTimer()));
		node0.deployEnsemble(DataPass.class);

		CustomPosition node1Pos = new CustomPosition();
		DEECoNode node1 = simulation.createNode(new OMNeTBroadcastDevice(), new PositionPlugin(0, 0), node1Pos);
		node1.deployComponent(new MoovingGrather("G", outputStream, omnet.getTimer(), node1Pos));
		node1.deployEnsemble(DataPass.class);

		simulation.start(2500*500);
	}
}
