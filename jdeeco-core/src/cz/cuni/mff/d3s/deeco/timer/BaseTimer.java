package cz.cuni.mff.d3s.deeco.timer;

import java.util.HashSet;
import java.util.Set;

/**
 * Shared base for common timer types. Implements basic timer behavior such as registering shutdown listeners.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 */
public abstract class BaseTimer implements Timer {
	/**
	 * Timer shutdown listener
	 */
	private Set<ShutdownListener> shutdownListeners = new HashSet<>();

	/**
	 * Registers new shutdown listener
	 * 
	 * @param listener
	 *            Shutdown listener to register
	 */
	public void addShutdownListener(ShutdownListener listener) {
		shutdownListeners.add(listener);
	}

	/**
	 * Runs registered shutdown listeners
	 */
	public void runShutdownListeners() {
		for (ShutdownListener listener : shutdownListeners) {
			listener.onShutdown();
		}
	}
}
