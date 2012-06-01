package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public abstract class Scheduler {

	protected List<SchedulableProcess> processes;
	protected KnowledgeManager km;
	protected boolean running;

	public Scheduler(KnowledgeManager km) {
		this.km = km;
		processes = new LinkedList<SchedulableProcess>();
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
			return processes.add(process);
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
			return processes.remove(process);
		return false;
	}

	public abstract void start();

	public abstract void stop();
}
