package cz.cuni.mff.d3s.deeco.task.provider;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProviderHolder;
import cz.cuni.mff.d3s.deeco.task.Task;

public abstract class PeriodicTaskProvider extends MonitorProviderHolder {
	
	public PeriodicTaskProvider(MonitorProvider monitorProvider) {
		super(monitorProvider);
	}
	
	public abstract Task getTask();
	public abstract long getPeriod();

}
