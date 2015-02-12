/**
 * 
 */
package cz.cuni.mff.d3s.deeco.scheduler;


/**
 * Listener interface for time events triggered by a simulation.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface SimulationTimeEventListener {

	/**
	 * Simulation callback method for the previous registration
	 * 
	 * @param time
	 *            current simulation time
	 * @throws NoExecutorAvailableException 
	 */
	void at(long time);
}
