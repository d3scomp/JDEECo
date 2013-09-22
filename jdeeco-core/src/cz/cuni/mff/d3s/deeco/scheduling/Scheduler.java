package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.TimeProvider;

public abstract class Scheduler implements JobExecutionListener, TimeProvider {

	public abstract void schedule(Job job);

	public abstract void start();

	public abstract void stop();
	
	public abstract boolean isStarted();

}
