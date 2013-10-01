package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public abstract class MonitorProviderHolder {
	
	protected final MonitorProvider monitorProvider;
	
	public MonitorProviderHolder(MonitorProvider monitorProvider) {
		this.monitorProvider = monitorProvider;
	}
}
