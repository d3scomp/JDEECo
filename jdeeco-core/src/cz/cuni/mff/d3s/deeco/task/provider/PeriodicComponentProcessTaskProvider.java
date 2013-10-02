package cz.cuni.mff.d3s.deeco.task.provider;

import cz.cuni.mff.d3s.deeco.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.task.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.task.Task;

public class PeriodicComponentProcessTaskProvider extends PeriodicTaskProvider {

	private final ComponentProcess componentProcess;
	private final String componentId;

	public PeriodicComponentProcessTaskProvider(
			ComponentProcess componentProcess, String componentId,
			MonitorProvider monitorProvider) {
		super(monitorProvider);
		this.componentProcess = componentProcess;
		this.componentId = componentId;
	}

	@Override
	public Task getTask() {
		ComponentProcessTask task = new ComponentProcessTask(componentProcess,
				componentId);
		task.setListener(monitorProvider.getProcessMonitor(task));
		return task;
	}

	@Override
	public long getPeriod() {
		return componentProcess.getSchedule().getPeriod();
	}

}
