package cz.cuni.mff.d3s.deeco.timer;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;

public interface Timer extends CurrentTimeProvider {
	/**
	 * Set next notification time for node
	 * 
	 * The timer is supposed to provide only one notification time per node.
	 * Calling this method twice at the same time will result in the discarding
	 * of the effect of the first call.
	 * 
	 * @param time
	 *            Notification time in ms
	 * @param listener
	 *            Notification listener
	 * @param eventName
	 *            The name of the event that will be fired by the timer. The
	 *            name should be the name of process represented by the event.
	 * @param node
	 *            Node to schedule the even at
	 */
	void notifyAt(long time, TimerEventListener listener, String eventName,
			DEECoContainer node);

	/**
	 * Add new event that is supposed to be executed immediately (e.g.
	 * processing of incoming data packet). Calling this method will cause the
	 * timer to wake if it sleeps or waits.
	 * 
	 * @param listener
	 *            Notification listener
	 * @param eventName
	 *            The name of the event that will be fired by the timer. The
	 *            name should be the name of process represented by the event.
	 * @param node
	 *            Node to schedule the even at
	 */
	void interruptionEvent(TimerEventListener listener, String eventName,
			DEECoContainer node);
}