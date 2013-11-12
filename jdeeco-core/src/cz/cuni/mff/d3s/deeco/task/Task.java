package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public abstract class Task {
	protected TaskTriggerListener listener;
	protected Scheduler scheduler;
	
	public Task(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public abstract void invoke(Trigger trigger) throws TaskInvocationException;

	protected abstract void registerTriggers();
	protected abstract void unregisterTriggers();
	
	public void setTriggerListener(TaskTriggerListener listener) {
		assert(listener != null);
		
		if (this.listener != null) {
			unsetTriggerListener();
		}
		
		this.listener = listener;
		registerTriggers();
	}
	
	public void unsetTriggerListener() {
		if (this.listener != null) {
			unregisterTriggers();			
			this.listener = null;
		}
	}

	/**
	 * Returns the period associated with the task. 
	 * @return Period in miliseconds or -1 when there is no period associated with the task.
	 */
	abstract public PeriodicTrigger getPeriodicTrigger();
}
