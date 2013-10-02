package cz.cuni.mff.d3s.deeco.task.provider;

import java.util.List;

import cz.cuni.mff.d3s.deeco.model.Trigger;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProviderHolder;

public abstract class TriggeredTaskProvider extends MonitorProviderHolder {

	public TriggeredTaskProvider(MonitorProvider monitorProvider) {
		super(monitorProvider);
	}

	public abstract List<Trigger> getTriggers();
	
}
