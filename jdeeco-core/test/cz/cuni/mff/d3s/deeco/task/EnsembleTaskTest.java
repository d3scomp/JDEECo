package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class EnsembleTaskTest {
	
	@Mock
	private EnsembleController instanceEnsemblingController;
	@Mock
	private Scheduler scheduler;
	
	private Task ensembleTask;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.ensembleTask = new EnsembleTask(instanceEnsemblingController, scheduler);
	}
	
	@Test
	@Ignore
	public void testSchedulingPeriod() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN getSchedulingPeriod is called
		// THEN it returns the period specified in the model
	}
	
	@Test
	@Ignore
	public void testTrigger() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN a trigger listener is registered
		// AND a trigger comes from the knowledge manager
		// THEN the task calls the registered listener
		
		// WHEN the listener is unregistered
		// AND a trigger comes from the knowledge manager
		// THEN the task does not call the registered listener
	}
	
	@Test
	@Ignore
	public void testEnsembleTaskInvoke() {
		// GIVEN an EnsembleTask initialized with an InstanceEnsemblingController
		// WHEN invoke on the task is called
		// THEN it gets the needed local knowledge
		// AND it retrieves the other knowledge managers
		// AND it gets knowledge from them
		// AND it executes the membership
		// AND it executes knowledge exchange for those for which the membership was satisfied 
		// AND it updates the instance knowledge with outputs of the knowledge exchange
		
		fail("Not implemented!");
	}	
}
