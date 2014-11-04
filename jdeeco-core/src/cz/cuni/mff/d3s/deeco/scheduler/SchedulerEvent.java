package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Wrapper class for all scheduler events
 * <p>
 * 
 * @author 	Andranik Muradyan 	<muradian@d3s.mff.cuni.cz>
 * @author 	Jaroslav Keznikl 	<keznikl@d3s.mff.cuni.cz>
 * 
 */
public class SchedulerEvent implements Comparable<SchedulerEvent> {
    /**
     * The state of this task, chosen from the constants below.
     */
    int state = VIRGIN;

    static final int VIRGIN = 0;
    static final int SCHEDULED = 1;
    static final int CANCELLED = 2;
    static final int EXECUTED = 3;
    static final int RUNNING = 4;    
    static final int FAILED = 5;    
    
    /**
     * Next execution time for this task in the format returned by
     * System.currentTimeMillis, assuming this task is scheduled for execution.
     * For repeating tasks, this field is updated prior to each task execution.
     */
    public long nextExecutionTime;

    /**
	 * For a periodic tasks indicates the start of the period which the
	 * {@link #nextExecutionTime} falls in.
	 */
    public long nextPeriodStart;
    
    /**
     * Indicator, whether the event is periodic (i.e., fixed-rate execution). 
     * It is set to true only if the trigger is a TimeTrigger with period > 0.
     */
    public final boolean periodic;
    
    /**
     * The actual task to be executed.
     */
    public final Task executable;
    
    /** 
     * The trigger associated with this event.
     */
    public final Trigger trigger;

	
    

    /**
     * Creates a new scheduler task.
     */
    public SchedulerEvent(Task task, Trigger trigger) {
    	this.executable = task;
    	this.trigger = trigger;    	
    	if ((trigger != null) && (trigger instanceof TimeTrigger) && (((TimeTrigger) trigger).getPeriod() > 0)) {
    		periodic = true;
    	} else {
    		periodic = false;
    	}
    }


	@Override
	public int compareTo(SchedulerEvent o) {
		if (this == o) return 0;
		else if (this.nextExecutionTime < o.nextExecutionTime) return -1;
		else if (this.nextExecutionTime > o.nextExecutionTime) return 1;
		else {
			int thisOrder = this.trigger instanceof TimeTrigger ? ((TimeTrigger)this.trigger).getOrder() : 0;
			int thatOrder = o.trigger instanceof TimeTrigger ? ((TimeTrigger)o.trigger).getOrder() : 0;

			if (thisOrder < thatOrder) return -1;
			else if (thisOrder > thatOrder) return 1;
			else return Integer.compare(this.hashCode(), o.hashCode());
		}
	}


	@Override
	public String toString() {
		return "SchedulerEvent [nextExecutionTime=" + nextExecutionTime
				+ ", executable=" + executable + ", trigger=" + trigger + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((executable == null) ? 0 : executable.hashCode());
		result = prime * result
				+ (int) (nextExecutionTime ^ (nextExecutionTime >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulerEvent other = (SchedulerEvent) obj;
		if (executable == null) {
			if (other.executable != null)
				return false;
		} else if (!executable.equals(other.executable))
			return false;
		if (nextExecutionTime != other.nextExecutionTime)
			return false;
		return true;
	}
	
	
	
}