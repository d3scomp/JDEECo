package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * Gets called when the trigger is triggered.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public interface TriggerListener {

	public void triggered(Trigger trigger);
}
