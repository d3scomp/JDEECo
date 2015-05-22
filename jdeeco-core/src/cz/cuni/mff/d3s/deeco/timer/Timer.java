package cz.cuni.mff.d3s.deeco.timer;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;

public interface Timer extends CurrentTimeProvider {
	/**
	 * Set next notification time for node
	 * 
	 * The timer is supposed to provide only one notification time per node. Calling this method twice at the same time
	 * will result in the discarding of the effect of the first call.
	 * 
	 * @param time
	 *            Notification time in ms
	 * @param listener
	 *            Notification listener
	 * @param node
	 *            Node to schedule the even at
	 */
	void notifyAt(long time, TimerEventListener listener, DEECoContainer node);
}