package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.Trigger;

public abstract class TriggeredTaskProvider extends MonitorProviderHolder {

	public TriggeredTaskProvider(MonitorProvider monitorProvider) {
		super(monitorProvider);
	}

	public abstract List<Trigger> getTriggers();
	
}
