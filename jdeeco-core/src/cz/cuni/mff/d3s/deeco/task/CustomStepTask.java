package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

public class CustomStepTask extends TimerTask {
	
	public CustomStepTask(Scheduler scheduler, TimerTaskListener taskListener, String taskName) {
		this(scheduler, taskListener, taskName, 1);
	}
	
	public CustomStepTask(Scheduler scheduler, TimerTaskListener taskListener, String taskName, long delay) {
		super(scheduler, taskListener, taskName, delay);		
	}
	
	public void scheduleNextExecutionAfter(long delay) {
		scheduler.addTask(new CustomStepTask(scheduler, taskListener, taskName, delay));
	}
}
