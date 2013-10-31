package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceEnsemblingController;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

public class EnsembleTask extends Task {

	InstanceEnsemblingController ensemblingController;
	
	public EnsembleTask(InstanceEnsemblingController ensemblingController, Scheduler scheduler) {
		super(ensemblingController.getEnsemble().getSchedule(), scheduler);
		
		this.ensemblingController = ensemblingController;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		
	}
}
