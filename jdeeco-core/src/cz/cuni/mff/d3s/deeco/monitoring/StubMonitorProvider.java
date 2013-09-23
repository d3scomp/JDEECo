package cz.cuni.mff.d3s.deeco.monitoring;

import java.util.List;

public class StubMonitorProvider implements MonitorProvider {

	@Override
	public Monitor getMonitor(String monitorId) {
		return null;
	}

	@Override
	public void addMonitor(Monitor monitor) {
		//Do nothing
	}

	@Override
	public void addAllMonitors(List<? extends Monitor> monitors) {
		//Do nothing	
	}

}
