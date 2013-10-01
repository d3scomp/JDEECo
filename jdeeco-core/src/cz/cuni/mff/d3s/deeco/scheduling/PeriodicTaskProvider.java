package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public abstract class PeriodicTaskProvider extends MonitorProviderHolder {
	
	public PeriodicTaskProvider(MonitorProvider monitorProvider) {
		super(monitorProvider);
	}
	
	public abstract Task getTask();
	public abstract long getPeriod();

}
