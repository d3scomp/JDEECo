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
		if( this.nextExecutionTime < o.nextExecutionTime ) return -1;
		else if( this.nextExecutionTime > o.nextExecutionTime ) return 1;
		else if (this == o) return 0;
		else return this.hashCode() < o.hashCode() ? 1 : -1;
	}


	@Override
	public String toString() {
		return "SchedulerEvent [nextExecutionTime=" + nextExecutionTime
				+ ", executable=" + executable + ", trigger=" + trigger + "]";
	}
	
	
	
}