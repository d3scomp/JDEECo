package cz.cuni.mff.d3s.jdeeco.network.convoy;

import org.junit.Rule;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.LoopbackBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DummyKnowledgePublisher;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyTest {	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException {
		ConvoyTest test = new ConvoyTest();

		test.testConvoyLoopback();
	}

	// #######################################
	//  LOOPBACK NETWORKING
	// #######################################
	@Test
	public void testConvoyLoopback() throws AnnotationProcessorException, InterruptedException, DEECoException {
		DiscreteEventTimer timer = new DiscreteEventTimer();
		
		LoopbackBroadcastDevice loopback = new LoopbackBroadcastDevice();
		
		// Create main application container
		DEECoNode deeco1 = new DEECoNode(timer, new Network(), loopback,
				new DummyKnowledgePublisher(), new KnowledgeInsertingStrategy());

		DEECoNode deeco2 = new DEECoNode(timer, new Network(), loopback,
				new DummyKnowledgePublisher(), new KnowledgeInsertingStrategy());

		// Deploy components and ensembles
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);

		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		timer.start(600000);
	}
}
