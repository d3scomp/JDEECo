package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessTrigger;

public abstract class Scheduler implements IScheduler {

	protected List<SchedulableProcess> periodicProcesses;
	protected List<SchedulableProcessTrigger> triggeredProcesses;
	protected boolean running;

	public Scheduler() {
		periodicProcesses = new ArrayList<SchedulableProcess>();
		triggeredProcesses = new ArrayList<SchedulableProcessTrigger>();
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(java.util.List)
	 */
	@Override
	public synchronized void add(List<? extends SchedulableProcess> processes) {
		for (SchedulableProcess sp : processes) {
			add(sp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(cz.cuni.mff.d3s.
	 * deeco.invokable.SchedulableProcess)
	 */
	@Override
	public synchronized void add(SchedulableProcess process) {
		if (process.scheduling instanceof ProcessTriggeredSchedule)
			triggeredProcesses.add(new SchedulableProcessTrigger(process));
		else {
			periodicProcesses.add(process);
			if (running) {
				startPeriodicProcess(process);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(java.util.List)
	 */
	@Override
	public synchronized void remove(List<SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				remove(sp);
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(cz.cuni.mff.d3s
	 * .deeco.invokable.SchedulableProcess)
	 */
	@Override
	public synchronized void remove(SchedulableProcess process) {
		if (!running)
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				for (SchedulableProcessTrigger tsp : triggeredProcesses)
					if (tsp.sp == process) {
						tsp.unregisterListener();
						triggeredProcesses
								.remove(new SchedulableProcessTrigger(process));
					} else
						periodicProcesses.remove(process);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getPeriodicProcesses()
	 */
	@Override
	public synchronized List<SchedulableProcess> getPeriodicProcesses() {
		return periodicProcesses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getTriggeredProcesses()
	 */
	@Override
	public synchronized List<SchedulableProcessTrigger> getTriggeredProcesses() {
		return triggeredProcesses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#clearAll()
	 */
	@Override
	public synchronized void clearAll() {
		if (running)
			stop();
		remove(periodicProcesses);
		for (SchedulableProcessTrigger tsp : triggeredProcesses) {
			remove(tsp.sp);
		}
	}

	protected abstract void startPeriodicProcess(SchedulableProcess process);
}
