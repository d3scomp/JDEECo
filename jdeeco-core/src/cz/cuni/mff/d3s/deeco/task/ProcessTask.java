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
		// TODO Auto-generated method stub
		
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
