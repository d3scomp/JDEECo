package cz.cuni.mff.d3s.deeco.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

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
	
	@Rule
	public final StandardOutputStreamLog  log = new StandardOutputStreamLog ();
	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException {
		new ConvoySimulationTest().testConvoy();
	}
	
	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException {

		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer(); // also "new WallTimeSchedulerNotifier()" 
 		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		 
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
		realm.start(2000);

		// THEN the follower prints out the following (as there is no network and the components cannot exchange data)
		assertThat(log.getLog(), containsString("Follower F: me = (1,1) leader = null"));
	}	

}
