package cz.cuni.mff.d3s.deeco.demo.convoy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECo;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.deeco.runtime.PluginDependencyException;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ConvoyTest {
	
	@Rule
	public final StandardOutputStreamLog  log = new StandardOutputStreamLog ();
	
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException {
		new ConvoyTest().testConvoy();
	}
	
	@Test
	public void testConvoy() throws AnnotationProcessorException, InterruptedException, DEECoException {
		
		/* create main application container */
		DEECo deeco = new DEECo();
		/* deploy components and ensembles */
		deeco.deployComponent(new Leader());
		deeco.deployComponent(new Follower());
		deeco.deployEnsemble(ConvoyEnsemble.class);
		
		deeco.start();
		Thread.sleep(2000);
		deeco.stop();
		
		// THEN the follower reaches his destination
		assertThat(log.getLog(), containsString("Follower F: me = (1,3)"));
	}	
}
