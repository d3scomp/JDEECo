package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public abstract class Task {
	protected TriggerListener listener;
	protected Scheduler scheduler;
	
	public Task(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public abstract void invoke() throws TaskInvocationException;

	protected abstract void registerTriggers();
	protected abstract void unregisterTriggers();
	
	// FIXME TB: The trigger listener between task and scheduler should not be the same as the one for the knowledge manager
	// The reasons are two:
	//   1) The KnowledgeManagersView should use listeners that identify in which shadowKM the trigger occured
	//   2) The scheduler should not be interested in the particular trigger
	public void setTriggerListener(TriggerListener listener) {
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

	abstract public long getSchedulingPeriod();
}
