package cz.cuni.mff.d3s.deeco.timer;

import java.util.LinkedList;
import java.util.List;

/**
 * Shared base for common timer types. Implements basic timer behavior such as registering shutdown and startup listeners.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 */
public abstract class BaseTimer implements Timer {
	/**
	 * Timer shutdown listener
	 */
	private List<ShutdownListener> shutdownListeners = new LinkedList<>();
	
	/**
	 * Timer startup listener
	 */
	private List<StartupListener> startupListeners = new LinkedList<>();

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
	 * Registers new startup listener
	 * 
	 * @param listener
	 *            Startup listener to register
	 */
	public void addStartupListener(StartupListener listener) {
		startupListeners.add(listener);
	}

	/**
	 * Runs registered shutdown listeners
	 */
	public void runShutdownListeners() {
		for (ShutdownListener listener : shutdownListeners) {
			listener.onShutdown();
		}
	}
	
	/**
	 * Runs registered shutdown listeners
	 */
	public void runStartupListeners() {
		for (StartupListener listener : startupListeners) {
			listener.onStartup();
		}
	}
}
