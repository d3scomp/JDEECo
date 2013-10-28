package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceProcess;

public class ProcessTask extends Task {
	
	InstanceProcess process;
	
	public ProcessTask(InstanceProcess process) {
		super(process.getProcess().getSchedule());
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		
	}
}
