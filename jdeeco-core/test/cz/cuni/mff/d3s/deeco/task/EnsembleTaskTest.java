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
		// WHEN a trigger listener (i.e. scheduler) is registered at the task
		// THEN the task registers a trigger listener on the knowledge manager

		// WHEN a trigger comes from the knowledge manager
		// THEN the task calls the registered listener
		
		// WHEN the listener (i.e. the scheduler) is unregistered
		// THEN the trigger is unregistered at the knowledge manager

		// NOTE that we don't test here the case the trigger is not delivered to the scheduler when spurious trigger from the knowledge manager comes.
		// This is because the task does the registration by directly passing the triggerListener to the knowledge manager and thus it has no control
		// over the delivery (from the knowledge manager to the scheduler).
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
