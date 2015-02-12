/**
 * 
 */
package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;


/**
 * Listener interface for time events triggered by a simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface TimerTaskListener {

	/**
	 * Simulation callback method for the previous registration
	 * 
	 * @param time
	 *            current simulation time
	 */
	void at(long time, Object triger);
	
	TimerTask getInitialTask(Scheduler scheduler);
}
