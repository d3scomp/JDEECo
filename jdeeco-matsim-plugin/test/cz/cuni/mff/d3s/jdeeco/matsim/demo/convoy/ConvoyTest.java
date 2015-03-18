package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;

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
public class ConvoyTest {
	
	@Rule
	public final StandardOutputStreamLog  log = new StandardOutputStreamLog ();
	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		new ConvoyTest().testConvoy();
	}
	
	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode deeco = realm.createNode(0);
		/* deploy components and ensembles */
		deeco.deployComponent(new Leader());
		deeco.deployComponent(new Follower());
		deeco.deployEnsemble(ConvoyEnsemble.class);
		
		/* WHEN simulation is performed */
		realm.start(2000);
		
		// THEN the follower reaches his destination
		assertThat(log.getLog(), containsString("Follower F: me = (1,3)"));
	}	

}
