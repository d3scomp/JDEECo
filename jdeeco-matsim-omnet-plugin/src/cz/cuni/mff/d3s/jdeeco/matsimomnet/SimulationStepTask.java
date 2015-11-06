package cz.cuni.mff.d3s.jdeeco.matsimomnet;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

public class SimulationStepTask extends TimerTask {
	public SimulationStepTask(Scheduler scheduler, TimerTaskListener taskListener) {
		this(scheduler, taskListener, 1);
	}
	
	public SimulationStepTask(Scheduler scheduler, TimerTaskListener taskListener, long delay) {
		super(scheduler, taskListener, "StepTask", delay);
		((TimeTrigger) this.trigger).setOrder(Integer.MIN_VALUE);
	}
	
	public void scheduleNextExecutionAfter(long delay) {
		scheduler.addTask(new SimulationStepTask(scheduler, taskListener, delay));
	}
}
