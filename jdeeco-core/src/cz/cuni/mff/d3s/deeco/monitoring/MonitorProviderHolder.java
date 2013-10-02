package cz.cuni.mff.d3s.deeco.monitoring;


public abstract class MonitorProviderHolder {
	
	protected final MonitorProvider monitorProvider;
	
	public MonitorProviderHolder(MonitorProvider monitorProvider) {
		this.monitorProvider = monitorProvider;
	}
}
