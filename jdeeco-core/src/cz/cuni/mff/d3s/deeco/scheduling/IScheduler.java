package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessTrigger;

public interface IScheduler {
	
	public boolean isRunning();

	public void add(List<? extends SchedulableProcess> processes);

	public void add(SchedulableProcess process);

	public void remove(List<SchedulableProcess> processes);

	public void remove(SchedulableProcess process);
	
	public List<SchedulableProcessTrigger> getTriggeredProcesses();
	
	public List<SchedulableProcess> getPeriodicProcesses();
	
	public void clearAll();

	public void start();

	public void stop();
}
