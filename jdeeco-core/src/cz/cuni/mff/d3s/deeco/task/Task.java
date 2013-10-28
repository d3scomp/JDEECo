package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public abstract class Task {
	
	SchedulingSpecification schedulingSpecification;
	NotificationsForScheduler listener;

	public Task(SchedulingSpecification schedulingSpecification) {
		this.schedulingSpecification = schedulingSpecification;
	}
		
	public abstract void invoke();

	public void setSchedulingNotificationTarget(NotificationsForScheduler listener) {
		this.listener = listener;
	}
	
	public long getSchedulingPeriod() {
		return schedulingSpecification.getPeriod();
	}
}
