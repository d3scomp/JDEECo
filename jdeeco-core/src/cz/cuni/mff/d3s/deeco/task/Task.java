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

	public abstract void registerTriggers();
	public abstract void unregisterTriggers();
	
	public void setSchedulingNotificationTarget(NotificationsForScheduler listener) {
		if (this.listener != null && listener == null) {
			unregisterTriggers();			
			this.listener = null;

		} else if (this.listener == null && listener != null) {
			this.listener = listener;
			registerTriggers();
		
		} else {
			this.listener = listener;
		}
	}
	
	public long getSchedulingPeriod() {
		return schedulingSpecification.getPeriod();
	}
}
