package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public abstract class Scheduler {

	protected List<SchedulableProcess> periodicProcesses;
	protected List<TriggeredSchedulableProcess> triggeredProcesses;
	public KnowledgeManager km;
	protected boolean running;	//TODO: is never set to true

	public Scheduler() {
		periodicProcesses = new ArrayList<SchedulableProcess>();
		triggeredProcesses = new ArrayList<TriggeredSchedulableProcess>();
		running = false;
		km = null;
	}
	
	public Scheduler(KnowledgeManager km) {
		this();
		this.km = km;
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
		if (!running) {
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				return triggeredProcesses.add(new TriggeredSchedulableProcess(process));
			else
				return periodicProcesses.add(process);
		}
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
				for (TriggeredSchedulableProcess tsp : triggeredProcesses)
					if (tsp.sp == process)
						return triggeredProcesses.remove(new TriggeredSchedulableProcess(process));
			else
				return periodicProcesses.remove(process);
		return false;
	}
	
	public void clearAll() {
		if (running)
			stop();
		unregister(periodicProcesses);
		for (TriggeredSchedulableProcess tsp: triggeredProcesses) {
			unregister(tsp.sp);
		}
	}
	
	public void setKnowledgeManager(Object km) {
		if (this.km != null)
			stop();
		if (km instanceof KnowledgeManager)
			this.km = (KnowledgeManager) km;
	}
	
	public void unsetKnowledgeManager(Object km) {
		if (this.km != null)
			stop();
		this.km = null;
	}

	public abstract void start();

	public abstract void stop();
}
