package cz.cuni.mff.d3s.deeco.timer;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;

public interface Timer extends CurrentTimeProvider {
	/**
	 * Interface for timer shutdown listeners
	 */
	interface ShutdownListener {
		void onShutdown();
	}

	/**
	 * Interface for timer startup listeners
	 */
	interface StartupListener {
		void onStartup();
	}

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
	 * @param eventName
	 *            The name of the event that will be fired by the timer. The name should be the name of process
	 *            represented by the event.
	 * @param node
	 *            Node to schedule the even at
	 */
	void notifyAt(long time, TimerEventListener listener, String eventName, DEECoContainer node);

	/**
	 * Add new event that is supposed to be executed immediately (e.g. processing of incoming data packet). Calling this
	 * method will cause the timer to wake if it sleeps or waits.
	 * 
	 * @param listener
	 *            Notification listener
	 * @param eventName
	 *            The name of the event that will be fired by the timer. The name should be the name of process
	 *            represented by the event.
	 * @param node
	 *            Node to schedule the even at
	 */
	void interruptionEvent(TimerEventListener listener, String eventName, DEECoContainer node);

	/**
	 * Adds timer shutdown listener
	 * 
	 * Shutdown listeners are executed when the timer reaches its time limit or is stopped from some other reason. The
	 * listeners are executed in the same order as added.
	 */
	void addShutdownListener(ShutdownListener listener);

	/**
	 * Adds timer startup listener
	 * 
	 * Startup listeners are executed right before the timer starts executing tasks. The listeners are executed in the
	 * same order as added.
	 */
	void addStartupListener(StartupListener listener);
}