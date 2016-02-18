package cz.cuni.mff.d3s.jdeeco.adaptation;

/**
 * Interface for communication between meta adaptation manager and managed managers.
 */
public interface AdaptationManager {

	/**
	 * AdaptationManager should run.
	 */
	void run();

	/**
	 * AdaptationManager should stop.
	 */
	void stop();

	/**
	 * Returns whether AdaptationManager is done yet.
	 * @return true if AdaptationManager is done yet
	 */
	boolean isDone();
}
