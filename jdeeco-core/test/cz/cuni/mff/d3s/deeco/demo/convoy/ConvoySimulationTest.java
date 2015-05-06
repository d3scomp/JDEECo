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
/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class ConvoySimulationTest {
	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		new ConvoySimulationTest().testConvoy(false);
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
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()" 
 		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		 
		/* create first deeco node */
		DEECoNode deeco1 = realm.createNode(0);
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader(outputStream));
		deeco1.deployEnsemble(ConvoyEnsemble.class);
		
		/* create second deeco node */
		DEECoNode deeco2 = realm.createNode(1);
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower(outputStream));
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.start(2000);

		// THEN the follower prints out the following (as there is no network and the components cannot exchange data)
		if (silent)
			assertThat(baos.toString(), containsString("Follower F: me = (1,1) leader = null"));
	}	

}
