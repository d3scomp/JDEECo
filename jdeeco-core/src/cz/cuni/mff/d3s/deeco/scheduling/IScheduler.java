package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public interface IScheduler {
	
	public boolean isRunning();

	public void register(List<? extends SchedulableProcess> processes);

	public boolean register(SchedulableProcess process);

	public void unregister(List<SchedulableProcess> processes);

	public boolean unregister(SchedulableProcess process);
	
	public List<TriggeredSchedulableProcess> getTriggeredProcesses();
	
	public List<SchedulableProcess> getPeriodicProcesses();
	
	public void clearAll();

	public void start();

	public void stop();
}
