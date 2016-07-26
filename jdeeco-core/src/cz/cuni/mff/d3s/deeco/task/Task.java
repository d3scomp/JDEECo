package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * Common ancestor for all tasks. Contains methods to be used (i) by the scheduler to learn when the task is to be scheduler, 
 * and (ii) by the executor to invoke the task.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public abstract class Task {
	protected TaskTriggerListener listener;
	protected Scheduler scheduler;
	protected final String taskName;
	
	public Task(Scheduler scheduler, String taskName) {
		this.scheduler = scheduler;
		this.taskName = taskName;
	}

	public String getName(){
		return taskName;
	}
	
	/**
	 * Invokes the task.
	 * 
	 * @param trigger the trigger that caused the task invocation. It is either a {@link PeriodicTrigger} in case this triggering
	 * is because of new period or a trigger given by task to the scheduler when invoking the trigger listener.
	 *  
	 * @throws TaskInvocationException signifies a problem in executing the task.
	 */
	public abstract void invoke(Trigger trigger) throws TaskInvocationException;

	/**
	 * Called by the this class when a trigger listener is set. This method is to be overridden in the subclasses, where it should register
	 * triggers in the corresponding knowledge manager(s).
	 */
	protected abstract void registerTriggers();
	/**
	 * Called by this class when a trigger listener is removed. This method is to be overridden in the subclasses, where it should unregister
	 * triggers in the respective knowledge manager(s).
	 */
	protected abstract void unregisterTriggers();

	/**
	 * Sets the listener by which the task notifies a scheduler about the fact that it has been triggered (by change in the knowledge).
	 * The task remembers only the last listener set. This is because typically only the scheduler uses this listener, thus one listener is enough.
	 */
	public void setTriggerListener(TaskTriggerListener listener) {
		assert(listener != null);
		
		if (this.listener != null) {
			unsetTriggerListener();
		}
		
		this.listener = listener;
		registerTriggers();
	}

	/**
	 * Removes the trigger listener
	 */
	public void unsetTriggerListener() {
		if (this.listener != null) {
			unregisterTriggers();			
			this.listener = null;
		}
	}

	/**
	 * Returns the time trigger associated with the task. 
	 */
	abstract public TimeTrigger getTimeTrigger();
}
