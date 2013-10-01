package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.knowledge.TimeProvider;

public abstract class Scheduler implements TimeProvider {

	public abstract void add(PeriodicTaskProvider taskProvider);
	
	public abstract void add(TriggeredTaskProvider taskProvider);
	
	public abstract void remove(PeriodicTaskProvider taskProvider);
	
	public abstract void remove(TriggeredTaskProvider taskProvider);

	public abstract void start();

	public abstract void stop();
	
	public abstract boolean isStarted();
}
