package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This class allows the user to check if the trigger is triggered
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

public abstract class TriggerListener {
	
//	protected Collection<Task> task;
	
	public abstract void triggered(Trigger trigger);

}
