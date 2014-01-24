package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Wrapper class for all periodic events
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
     * Period in milliseconds for repeating tasks.  A positive value indicates
     * fixed-rate execution.  A value of 0 indicates a non-repeating task.
     */
    public long period = 0;
    
    /**
     * The actual task to be executed.
     */
    public Task executable;
    
    /** 
     * The trigger associated with this event.
     */
    public Trigger trigger;
    

    /**
     * Creates a new scheduler task.
     */
    public SchedulerEvent(Task task, Trigger trigger) {
    	this.executable = task;
    	this.trigger = trigger;
    }


	@Override
	public int compareTo(SchedulerEvent o) {
		if( this.nextExecutionTime < o.nextExecutionTime ) return -1;
		else if( this.nextExecutionTime > o.nextExecutionTime ) return 1;
		else return 0;
	}
	
}