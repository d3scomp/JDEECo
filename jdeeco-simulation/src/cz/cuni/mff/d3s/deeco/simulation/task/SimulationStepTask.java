package cz.cuni.mff.d3s.deeco.simulation.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.SimulationStepListener;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

public class SimulationStepTask extends Task {

	private final TimeTrigger trigger;
	private final SimulationStepListener simulationStepListener;
	
	public SimulationStepTask(Scheduler scheduler, SimulationStepListener simulationStepListener) {
		this(scheduler, simulationStepListener, 1);
	}
	
	public SimulationStepTask(Scheduler scheduler, SimulationStepListener simulationStepListener, long delay) {
		super(scheduler);		

		this.trigger = new TimeTriggerExt();
		this.trigger.setOffset(delay);
		this.trigger.setPeriod(0);
		this.simulationStepListener = simulationStepListener;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		simulationStepListener.at(scheduler.getCurrentTime(), this);
	}
	
	public void scheduleNextExecutionAfter(long delay) {
		scheduler.addTask(new SimulationStepTask(scheduler, simulationStepListener, delay));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#getPeriodicTrigger()
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		return trigger;
	}
}
