/**
 * 
 */
package cz.cuni.mff.d3s.deeco.timer;

import cz.cuni.mff.d3s.deeco.scheduler.NoExecutorAvailableException;


/**
 * Listener interface for time events triggered by a simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface TimerEventListener {

	/**
	 * Simulation callback method for the previous registration
	 * 
	 * @param time
	 *            current simulation time
	 * @throws NoExecutorAvailableException 
	 */
	void at(long time);
}
