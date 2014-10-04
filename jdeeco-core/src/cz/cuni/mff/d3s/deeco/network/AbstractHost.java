package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public abstract class AbstractHost implements HostKnowledgeDataHandler, CurrentTimeProvider {

	protected final String id;
	protected final CurrentTimeProvider timeProvider;
	
	protected AbstractHost(String id, CurrentTimeProvider timeProvider) {
		this.id = id;
		this.timeProvider = timeProvider;
	}
	
	public String getHostId() {
		return id;
	}
	
	public long getCurrentMilliseconds() {
		return timeProvider.getCurrentMilliseconds();
	}
}
