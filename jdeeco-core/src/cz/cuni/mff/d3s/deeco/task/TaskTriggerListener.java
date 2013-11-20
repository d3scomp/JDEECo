package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;


/**
 * Gets called by the task when a trigger of the task occurs. This class is to be typically implemented by the scheduler, so that it can schedule
 * the task as the result of the task trigger.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public interface TaskTriggerListener {
	public void triggered(Task task, Trigger trigger);
}
