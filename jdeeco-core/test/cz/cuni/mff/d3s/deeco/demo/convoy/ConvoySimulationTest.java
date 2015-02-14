package cz.cuni.mff.d3s.deeco.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECo;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoSimulationRealm;
import cz.cuni.mff.d3s.deeco.runtime.SimulationSchedulerNotifier;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.DiscreteEventSchedulerNotifier;
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
		SimulationSchedulerNotifier simulationSchedulerNotifier = new DiscreteEventSchedulerNotifier(); // also "new WallTimeSchedulerNotifier()" 
 		DEECoSimulationRealm realm = new DEECoSimulationRealm(simulationSchedulerNotifier);
		 
		/* create first deeco node */
		DEECo deeco1 = realm.createNode();
		/* deploy components and ensembles */
		deeco1.deployComponent(new Leader());
		deeco1.deployEnsemble(ConvoyEnsemble.class);
		
		/* create second deeco node */
		DEECo deeco2 = realm.createNode();
		/* deploy components and ensembles */
		deeco2.deployComponent(new Follower());
		deeco2.deployEnsemble(ConvoyEnsemble.class);

		/* WHEN simulation is performed */
		realm.setTerminationTime(2000);
		realm.start();

		// THEN the follower prints out the following (as there is no network and the components cannot exchange data)
		assertThat(log.getLog(), containsString("Follower F: me = (1,1) leader = null"));
	}	

}
