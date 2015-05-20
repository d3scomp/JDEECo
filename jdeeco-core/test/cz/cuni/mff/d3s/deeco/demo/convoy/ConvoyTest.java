package cz.cuni.mff.d3s.deeco.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
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
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.ConvoyEnsemble;
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.Follower;
import cz.cuni.mff.d3s.jdeeco.core.demo.convoy.Leader;
/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class ConvoyTest {
	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		new ConvoyTest().testConvoy(false);
	}
	
	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		testConvoy(true);
	}
	
	private void testConvoy(boolean silent) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {	

		// In silent mode the output is kept in ByteArrayOutputStream and then tested
		// whether it's correct. In non-silent mode the output is not tested, but printed to console.
		PrintStream outputStream;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode deeco = realm.createNode(0);
		/* deploy components and ensembles */
		
		deeco.deployComponent(new Leader(outputStream, simulationTimer));
		deeco.deployComponent(new Follower(outputStream, simulationTimer));
		deeco.deployEnsemble(ConvoyEnsemble.class);
		
		/* WHEN simulation is performed */
		realm.start(10000);
		
		// THEN the follower reaches his destination
		if (silent)
			assertThat(baos.toString(), containsString("Follower F: me = (1,3)"));
	}	

}
