package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SingleThreadedExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeConfigurationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetWallTimeScheduler() {
		// WHEN a configuration with WALL_TIME scheduling is created
		RuntimeConfiguration cnf = new RuntimeConfiguration(Scheduling.WALL_TIME, null, null);
		// THEN an instance of LocalTimeScheduler is returned by getScheduler()
		Scheduler s = cnf.getScheduler();
		assertTrue(s instanceof LocalTimeScheduler);		
	}
	
	@Test
	public void testGetSchedulerReturnsTheSameInstance() {		
		// WHEN the getScheduler() is repeatedly called on a properly set-up configuration
		RuntimeConfiguration cnf = new RuntimeConfiguration(Scheduling.WALL_TIME, null, null);
		Scheduler s = cnf.getScheduler();
		Scheduler s2 = cnf.getScheduler();
		// THEN the it returns always the same instance 
		assertSame(s, s2);
	}
	
	@Test
	public void testGetSingleThreadedExecutor() {
		// WHEN a configuration with SINGLE_THREADED execution is created
		RuntimeConfiguration cnf = new RuntimeConfiguration(null, null, Execution.SINGLE_THREADED);
		// THEN an instance of SingleThreadedExecutor is returned by getExecutor()
		Executor e = cnf.getExecutor();
		assertTrue(e instanceof SingleThreadedExecutor);		
	}
	
	@Test
	public void testGetExedutorReturnsTheSameInstance() {		
		// WHEN the getExecutor() is repeatedly called on a properly set-up configuration
		RuntimeConfiguration cnf = new RuntimeConfiguration(null, null, Execution.SINGLE_THREADED);
		Executor e = cnf.getExecutor();
		Executor e2 = cnf.getExecutor();
		// THEN the it returns always the same instance 
		assertSame(e, e2);
	}
	
	
	@Test
	public void testGetKnowledgeManagerRegistryReturnsTheSameInstance() {		
		// WHEN the getKnowledgeManagerRegistry() is repeatedly called on a properly set-up configuration
		RuntimeConfiguration cnf = new RuntimeConfiguration(null, null, null);
		KnowledgeManagerRegistry r = cnf.getKnowledgeManagerRegistry();
		KnowledgeManagerRegistry r2 = cnf.getKnowledgeManagerRegistry();
		// THEN the it returns always the same instance 
		assertSame(r, r2);
	}
	

}
