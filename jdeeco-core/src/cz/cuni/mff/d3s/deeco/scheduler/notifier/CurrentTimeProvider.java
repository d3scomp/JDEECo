package cz.cuni.mff.d3s.deeco.scheduler.notifier;

/**
 * Gives access to current time in milliseconds.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface CurrentTimeProvider {
	/**
	 * Returns current time in milliseconds.
	 */
	public long getCurrentMilliseconds();
}
