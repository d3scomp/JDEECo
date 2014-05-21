package cz.cuni.mff.d3s.deeco.simulation.task;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

public class SimulationStepTask extends Task {

	private final TimeTrigger trigger;
	private final SimulationTimeEventListener simulationStepListener;
	
	public SimulationStepTask(Scheduler scheduler, SimulationTimeEventListener simulationStepListener, long period, String host) {
		super(scheduler);		

		this.trigger = new TimeTriggerExt();
		this.trigger.setOffset(0);
		this.trigger.setPeriod(period);
		this.simulationStepListener = simulationStepListener;
		
		Log.i(String.format("SimulationStepTask at %s with simulation step %d", host, period));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		simulationStepListener.at(scheduler.getCurrentTime());
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
