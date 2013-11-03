package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class EnsembleMemberTask extends Task {

	EnsembleController ensembleController;
	
	public EnsembleMemberTask(EnsembleController ensembleController, Scheduler scheduler) {
		super(scheduler);
		
		this.ensembleController = ensembleController;
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

	public long getSchedulingPeriod() {
		return ensembleController.getEnsembleDefinition().getMemberSchedulingSpecification().getPeriod();
	}
}
