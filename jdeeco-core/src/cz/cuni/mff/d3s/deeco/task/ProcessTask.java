package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class ProcessTask extends Task {
	
	ComponentProcess componentProcess;
	
	public ProcessTask(ComponentProcess componentProcess, Scheduler scheduler) {
		super(scheduler);
		this.componentProcess = componentProcess;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() {
		// Obtain parameters from the knowledge
		
		// Create holders for the *OUT parameters
		// TODO: We could have an option of not creating the wrapper. That would make it easier to work with mutable out types.
		// TODO: We need some way of handling insertions/deletions in a hashmap.

		// Construct the parameters for the process method
		
		// Call the process method
		
		// Create a changeset
		
		// Write the changeset back to the knowledge
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	protected void registerTriggers() {
		assert(listener != null);
		
		KnowledgeManager km = componentProcess.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : componentProcess.getSchedulingSpecification().getTriggers()) {
			km.register(trigger, listener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		KnowledgeManager km = componentProcess.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : componentProcess.getSchedulingSpecification().getTriggers()) {
			km.unregister(trigger, listener);
		}		
	}

	public long getSchedulingPeriod() {
		return componentProcess.getSchedulingSpecification().getPeriod();
	}
}
