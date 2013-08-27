package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;

public abstract class Scheduler implements JobExecutionListener {

	public abstract void schedule(Job job);

	public abstract void start();

	public abstract void stop();
	
	public abstract boolean isStarted();

}
