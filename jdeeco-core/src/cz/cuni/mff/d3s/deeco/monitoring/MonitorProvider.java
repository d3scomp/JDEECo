package cz.cuni.mff.d3s.deeco.monitoring;

import java.util.List;


public interface MonitorProvider {
	Monitor getMonitor(String monitorId);
	void addMonitor(Monitor monitor);
	void addAllMonitors(List<? extends Monitor> monitors);
}
