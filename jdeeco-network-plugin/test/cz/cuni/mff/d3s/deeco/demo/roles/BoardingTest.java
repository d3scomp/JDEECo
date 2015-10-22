package cz.cuni.mff.d3s.deeco.demo.roles;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.RebroadcastStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

public class BoardingTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		new BoardingTest().testBoarding(false);
	}
	
	@Test
	public void testBoarding() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		testBoarding(true);
	}
	
	private void testBoarding(boolean silent) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			BoardingEnsemble.outputStream = new PrintStream(baos);
		} else {
			BoardingEnsemble.outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice(1, 0, 0, 4096));
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(RebroadcastStrategy.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode node1 = realm.createNode(0);
		node1.deployComponent(new Car("Audi A4", 4));
		node1.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node2 = realm.createNode(1);
		node2.deployComponent(new Car("Skoda Felicia", 0));
		node2.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node3 = realm.createNode(2);
		node3.deployComponent(new Car("Seat Ibiza", 1));
		node3.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node4 = realm.createNode(3);
		node4.deployComponent(new Bus("142", 42));
		node4.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node5 = realm.createNode(4);
		node5.deployComponent(new Bus("119", 0));
		node5.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node6 = realm.createNode(5);
		node6.deployComponent(new Building("Valdstejnsky palac", 150));
		node6.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node7 = realm.createNode(6);
		node7.deployComponent(new Person("Chuck", "Norris", 1));
		node7.deployEnsemble(BoardingEnsemble.class);
		DEECoNode node8 = realm.createNode(7);
		node8.deployComponent(new Person("Milos", "Zeman", 2)); // Milos Zeman requires two seats
		node8.deployEnsemble(BoardingEnsemble.class);
		
		/* WHEN simulation is performed */
		realm.start(1000);
		
		if (silent) {
			assertThat(baos.toString(), containsString("Chuck Norris can go by Car Audi A4"));
			assertThat(baos.toString(), containsString("Chuck Norris can go by Car Seat Ibiza"));
			assertThat(baos.toString(), containsString("Chuck Norris can go by Bus 142"));
			assertThat(baos.toString(), containsString("Milos Zeman can go by Car Audi A4"));
			assertThat(baos.toString(), containsString("Milos Zeman can go by Bus 142"));
			
			assertThat(baos.toString(), not(containsString("can go by Car Skoda Felicia"))); // nobody fits to Skoda Felicia, it's full!
			assertThat(baos.toString(), not(containsString("can go by Bus 119"))); // also the bus 119 has no free space
			assertThat(baos.toString(), not(containsString("Milos Zeman can go by Car Seat Ibiza"))); // no place for Milos Zeman in Ibiza
			
			// buildings do not implement VehicleRole, therefore can't be boarded,
			// though they have all the necessary fields (counterexample to duck typing)
			assertThat(baos.toString(), not(containsString("can go by Building Valdstejnsky palac")));
		}
	}

}
