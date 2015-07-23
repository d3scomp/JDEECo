package cz.cuni.mff.d3s.deeco.demo.intelligent;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

/**
 * A demo of the {@link EnsembleFactory}-oriented ensemble deployment functionality. Also serves as an integration test verifying
 * that ensemble formation and subsequent knowledge transfer is performed.
 * @author Filip Krijt
 *
 */
public class DummyEnsemblesTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		new DummyEnsemblesTest().testEnsembles(false);
	}
	
	@Test
	public void testEnsembles() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		testEnsembles(true);
	}
	
	private void testEnsembles(boolean silent) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			SimpleEnsembleFactory.outputStream = new PrintStream(baos);
		} else {
			SimpleEnsembleFactory.outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode deeco = realm.createNode(0);
		/* deploy components and an ensemble factory */
		
		SimpleEnsembleFactory factory = new SimpleEnsembleFactory();
		
		List<Robot> robots = new ArrayList<Robot>(Arrays.asList(new Robot("Wall-E"), new Robot("Gizmo"), new Robot("R2")));
		
		for(Robot r : robots) {
			deeco.deployComponent(r);
		}	
			
		deeco.deployEnsembleFactory(factory);		
		
		// Add another component for testing purposes once the factory is deployed
		Robot omnius = new Robot("Omnius"); 
		robots.add(omnius);
		deeco.deployComponent(omnius);
		
		// Run the simulation
		realm.start(999);
		
		if (silent) {
			String out = baos.toString().trim();			
			// Formation should be requested for each robot as many times as the formation period fits in the simulation run time
			int scheduleCount = robots.size() * ((999 - factory.getSchedulingOffset()) / factory.getSchedulingPeriod() + 1);
			assertEquals(scheduleCount, factory.formationCount);
			
			// The output should be strictly alternating between formation and exchange, with exactly scheduleCount occurences
			String desiredOutput = String.join(System.lineSeparator(), Collections.nCopies(scheduleCount, 
					"Ensemble formation requested!" + System.lineSeparator() + "Knowledge exchange performed!"));
			
			assertEquals(desiredOutput, out);				
		}
	}
}
