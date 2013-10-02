package cz.cuni.mff.d3s.deeco.task.provider;

import java.util.List;

import cz.cuni.mff.d3s.deeco.model.Ensemble;
import cz.cuni.mff.d3s.deeco.model.Trigger;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.Task;

public class TriggeredEnsembleTaskProvider extends TriggeredTaskProvider {
	private final Ensemble ensemble;

	public TriggeredEnsembleTaskProvider(Ensemble ensemble,
			MonitorProvider monitorProvider) {
		super(monitorProvider);
		this.ensemble = ensemble;
	}

	public Task getTask(String coordinator, String member) {
		EnsembleTask task = new EnsembleTask(ensemble, coordinator, member);
		task.setListener(monitorProvider.getExchangeMonitor(task));
		return task;
	}

	@Override
	public List<Trigger> getTriggers() {
		return ensemble.getSchedule().getTriggers();
	}
}
