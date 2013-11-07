package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;


/**
 * Gets called when a trigger of the task occurs.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public interface TaskTriggerListener {
	public void triggered(Task task, Trigger trigger);
}
