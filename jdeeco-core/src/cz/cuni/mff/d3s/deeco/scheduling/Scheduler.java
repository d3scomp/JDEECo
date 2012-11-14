package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public abstract class Scheduler implements IScheduler {

	protected List<SchedulableProcess> periodicProcesses;
	protected List<TriggeredSchedulableProcess> triggeredProcesses;
	protected boolean running;

	public Scheduler() {
		periodicProcesses = new ArrayList<SchedulableProcess>();
		triggeredProcesses = new ArrayList<TriggeredSchedulableProcess>();
		running = false;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(java.util.List)
	 */
	@Override
	public void register(List<? extends SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				register(sp);
			}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess)
	 */
	@Override
	public boolean register(SchedulableProcess process) {
		if (!running) {
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				return triggeredProcesses.add(new TriggeredSchedulableProcess(
						process));
			else
				return periodicProcesses.add(process);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(java.util.List)
	 */
	@Override
	public void unregister(List<SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				unregister(sp);
			}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess)
	 */
	@Override
	public boolean unregister(SchedulableProcess process) {
		if (!running)
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				for (TriggeredSchedulableProcess tsp : triggeredProcesses)
					if (tsp.sp == process) {
						tsp.unregisterListener();
						return triggeredProcesses
								.remove(new TriggeredSchedulableProcess(process));
					} else
						return periodicProcesses.remove(process);
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getPeriodicProcesses()
	 */
	@Override
	public List<SchedulableProcess> getPeriodicProcesses() {
		return periodicProcesses;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getTriggeredProcesses()
	 */
	@Override
	public List<TriggeredSchedulableProcess> getTriggeredProcesses() {
		return triggeredProcesses;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#clearAll()
	 */
	@Override
	public void clearAll() {
		if (running)
			stop();
		unregister(periodicProcesses);
		for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
			unregister(tsp.sp);
		}
	}
}
