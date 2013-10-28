package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceEnsemblingController;

public class EnsembleTask extends Task {

	InstanceEnsemblingController ensemblingController;
	
	public EnsembleTask(InstanceEnsemblingController ensemblingController) {
		super(ensemblingController.getEnsemble().getSchedule());
		
		this.ensemblingController = ensemblingController;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		
	}
}
