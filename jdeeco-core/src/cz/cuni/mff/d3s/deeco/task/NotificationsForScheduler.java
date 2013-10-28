package cz.cuni.mff.d3s.deeco.task;

/**
 * Gets called when the trigger is triggered or when some other event happens (e.g. when period of the task changes).
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public interface NotificationsForScheduler {

	public void triggered(Task task);
}
