package cz.cuni.mff.d3s.deeco.demo.roles;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogWritersMock;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

/**
 * Program that runs the simulation. When starting as Java application (using the main method) it prints
 * to console. When started as JUnit test, the output is redirected and checked for correctness.
 * 
 * See {@link BoardingEnsemble} for more details about this demo.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class BoardingSingleNodeTest {

	RuntimeLogWritersMock runtimeLogWriters;

	@Before
	public void setUp() throws IOException{
		runtimeLogWriters = new RuntimeLogWritersMock();
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		new BoardingSingleNodeTest().testBoarding(false);
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
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode deeco = realm.createNode(0, runtimeLogWriters);
		/* deploy components and ensembles */
		
		deeco.deployComponent(new Car("Audi A4", 4));
		deeco.deployComponent(new Car("Skoda Felicia", 0));
		deeco.deployComponent(new Car("Seat Ibiza", 1));
		deeco.deployComponent(new Bus("142", 42));
		deeco.deployComponent(new Bus("119", 0));
		deeco.deployComponent(new Building("Valdstejnsky palac", 150));
		deeco.deployComponent(new Person("Chuck", "Norris", 1));
		deeco.deployComponent(new Person("Milos", "Zeman", 2)); // Milos Zeman requires two seats
		deeco.deployEnsemble(BoardingEnsemble.class);
		
		/* WHEN simulation is performed */
		realm.start(999);
		
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
