package cz.cuni.mff.d3s.deeco.scheduler.notifier;

/**
 * TODO(Ilias): Merge this interface with the SchedulerNotifier interface before releasing jDEECo 3.0.
 * 
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
