package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public abstract class Scheduler {

	protected List<SchedulableProcess> periodicProcesses;
	protected List<TriggeredSchedulableProcess> triggeredProcesses;
	protected boolean running;

	public Scheduler() {
		periodicProcesses = new ArrayList<SchedulableProcess>();
		triggeredProcesses = new ArrayList<TriggeredSchedulableProcess>();
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public void register(List<? extends SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				register(sp);
			}
	}

	public boolean register(SchedulableProcess process) {
		if (!running)
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				return triggeredProcesses.add(new TriggeredSchedulableProcess(process));
			else
				return periodicProcesses.add(process);
		return false;
	}

	public void unregister(List<SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				unregister(sp);
			}
	}

	public boolean unregister(SchedulableProcess process) {
		if (!running)
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				return triggeredProcesses.remove(new TriggeredSchedulableProcess(process));
			else
				return periodicProcesses.remove(process);
		return false;
	}

	public abstract void start();

	public abstract void stop();
}
