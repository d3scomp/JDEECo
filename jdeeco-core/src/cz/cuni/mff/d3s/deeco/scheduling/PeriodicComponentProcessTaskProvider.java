package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;

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
