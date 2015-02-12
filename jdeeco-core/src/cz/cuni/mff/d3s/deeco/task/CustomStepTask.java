package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

public class CustomStepTask extends TimerTask {
	
	public CustomStepTask(Scheduler scheduler, TimerTaskListener taskListener) {
		this(scheduler, taskListener, 1);
	}
	
	public CustomStepTask(Scheduler scheduler, TimerTaskListener taskListener, long delay) {
		super(scheduler, taskListener, delay);		
	}
	
	public void scheduleNextExecutionAfter(long delay) {
		scheduler.addTask(new CustomStepTask(scheduler, taskListener, delay));
	}
}
